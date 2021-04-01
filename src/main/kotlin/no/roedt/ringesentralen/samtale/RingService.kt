package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Valg21SpesifikkeResultat
import java.sql.Timestamp
import javax.enterprise.context.ApplicationScoped

interface RingService {
    fun hentNestePersonAaRinge(request: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse?
    fun startSamtale(request: AutentisertStartSamtaleRequest)
    fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest)
    fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): NestePersonAaRingeResponse
}

@ApplicationScoped
class RingServiceBean(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val oppslagRepository: OppslagRepository,
    val samtaleRepository: PersistentSamtaleRepository,
    val oppfoelgingValg21Repository: OppfoelgingValg21Repository,
    val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder,
    val lokallagRepository: LokallagRepository,
): RingService {

    override fun hentNestePersonAaRinge(request: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse? =
        hentFoerstePerson(request)
            ?.let { it as Int }
            ?.let { personRepository.findById(it.toLong()) }
            ?.let { toResponse(it) }
            ?.also { oppslagRepository.persist(Oppslag(ringt = it.person.id.toInt(), ringerHypersysId = request.userId() )) }

    private fun hentFoerstePerson(request: AutentisertNestePersonAaRingeRequest): Any? {
        return if (request.modus == Modus.velgere) {
            return hentNestePerson(getPerson(request.userId))
        } else nesteMedlemAaRingeFinder.hentIDForNesteMedlemAaRinge(getPerson(request.userId), request.userId)
    }

    fun getPerson(userId: UserId): Person = personRepository.find("hypersysID", userId.userId).firstResult()

    private fun hentNestePerson(ringer: Person) = databaseUpdater.getResultList(
        """SELECT v.id FROM v_personerSomKanRinges v 
                WHERE fylke = ${ringer.fylke} 
                AND hypersysID is null 
                ORDER BY ABS(lokallag-'${ringer.lokallag}') ASC, 
                brukergruppe = ${GroupID.PrioritertAaRinge.nr} DESC,
                v.hypersysID DESC""")
        .firstOrNull()

    private fun getTidlegareSamtalarMedDennePersonen(oppringtNummer: String): List<Samtale> =
        databaseUpdater.getResultList("SELECT resultat, ringerNavn, datetime, kommentar, ringtNavn FROM `v_samtalerResultat` WHERE oppringtNummer = '$oppringtNummer'")
            .map { it as Array<*> }
            .map { Samtale(
                resultat = it[0] as String,
                ringer = it[1] as String,
                tidspunkt = (it[2] as Timestamp).toString(),
                kommentar = it[3] as String,
                ringtNummer = oppringtNummer,
                ringtNavn = it[4] as String
            ) }
            .toList()

    override fun startSamtale(request: AutentisertStartSamtaleRequest) {
        val ringerId = hypersysIDTilRingerId(request.userId)

        samtaleRepository.persist(
            PersistentSamtale(
                ringt = request.skalRingesID().toInt(),
                ringer = ringerId.toString().toInt(),
                resultat = Resultat.Samtale_startet.nr,
                kommentar = "Starter samtale"
            ))
    }

    override fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest) {
        val request = autentisertRequest.request
        assert(request.isGyldigResultat())
        val ringer = hypersysIDTilRingerId(autentisertRequest.userId).toString().toInt()
        samtaleRepository.persist(
            PersistentSamtale(
                ringt = request.ringtID.toInt(),
                ringer = ringer,
                resultat = request.resultat.nr,
                kommentar = request.kommentar
            ))
        lagreResultat(getNesteGroupID(request), request, ringer)
    }

    private fun getNesteGroupID(request: ResultatFraSamtaleRequest): GroupID? {
        return when {
            request.resultat.nesteGroupID != null -> request.resultat.nesteGroupID
            erFleireEnnToIkkeSvar(request) -> GroupID.Ferdigringt
            else -> null
        }
    }

    private fun lagreResultat(nesteGroupID: GroupID?, request: ResultatFraSamtaleRequest, ringer: Int) {
        if (request.skalRegistrere()) {
            registrerValg21SpesifikkeResultat(request)
        }
        if (isBrukerEllerVenterPaaGodkjenning(ringer)) return
        nesteGroupID?.nr?.let { personRepository.update("groupID=?1 where id=?2", it, request.ringtID) }
    }

    fun isBrukerEllerVenterPaaGodkjenning(ringer: Int) =
        GroupID.isBrukerEllerVenter(personRepository.find("ringer=?1", ringer).singleResult<Person>().groupID)

    override fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): NestePersonAaRingeResponse {
        request.validate()
        val oppringtNummer = request.ringtNummer()
        val personSomRingerTilbake: Person = personRepository.find("telefonnummer", oppringtNummer).firstResult()

        startSamtale(
            AutentisertStartSamtaleRequest(
                userId = request.userId,
                startSamtaleRequest = StartSamtaleRequest(
                    skalRingesID = personSomRingerTilbake.id
                ),
                modus = Modus.velgere
            ))
        return toResponse(personSomRingerTilbake)
    }

    private fun toResponse(it: Person) =
        NestePersonAaRingeResponse(
            person = it,
            lokallagNavn = lokallagRepository.findById(it.lokallag.toLong()).navn,
            tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1")
        )

    private fun erFleireEnnToIkkeSvar(request: ResultatFraSamtaleRequest): Boolean {
        val resultat: List<Int> = samtaleRepository.list("ringt=?1 and resultat=${Resultat.Ikke_svar.nr}", request.ringtID).map { it.resultat }
        val fleireEnnToIkkeSvar: Boolean = resultat.filter { it == 0 }.count() > 2
        val ingenSvar: Boolean = resultat.filter { it != Resultat.Ikke_svar.nr && it != Resultat.Samtale_startet.nr }.count() == 0
        return ingenSvar && fleireEnnToIkkeSvar && request.resultat == Resultat.Ikke_svar
    }

    private fun registrerValg21SpesifikkeResultat(request: ResultatFraSamtaleRequest) {
        val resultat = request.modusspesifikkeResultat as Valg21SpesifikkeResultat

        oppfoelgingValg21Repository.persist(
            OppfoelgingValg21(
                personId = request.ringtID.toInt(),
                koronaprogram = resultat.vilHaKoronaprogram,
                merAktiv = resultat.vilBliMerAktiv,
                valgkampsbrev = resultat.vilHaValgkampsbrev,
                vilIkkeBliRingt = request.vilIkkeBliRingt,
                vilHaMedlemsLink = resultat.vilHaMedlemsLink,
                vilHaNyhetsbrevLink = resultat.vilHaNyhetsbrevLink
            )
        )
    }

    fun hypersysIDTilRingerId(userId: UserId) =
        databaseUpdater.getResultList(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).first()
}