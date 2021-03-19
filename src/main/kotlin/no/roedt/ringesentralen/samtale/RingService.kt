package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.KoronaspesifikkeResultat
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import org.eclipse.microprofile.jwt.JsonWebToken
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
    val oppfoelgingKoronaRepository: OppfoelgingKoronaRepository,
    val hypersysService: HypersysService,
    val modelConverter: ModelConverter
): RingService {

    override fun hentNestePersonAaRinge(request: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse? =
        hentFoerstePerson(request)
            ?.let { it as Int }
            ?.let { personRepository.findById(it.toLong()) }
            ?.let { NestePersonAaRingeResponse(person = it, tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1"))}
            ?.also { oppslagRepository.persist(Oppslag(ringt = it.person.id.toInt(), ringerHypersysId = request.userId() )) }

    private fun hentFoerstePerson(request: AutentisertNestePersonAaRingeRequest): Any? {
        val ringer = getPerson(request.userId)
        return if (request.modus == Modus.velgere) {
            return hentNestePerson(ringer, "AND hypersysID is null ")
        } else hentIDForNesteMedlemAaRinge(ringer, request.userId, request.jwt)
    }

    private fun hentIDForNesteMedlemAaRinge(ringer: Person, userId: UserId, jwt: JsonWebToken): Any? {
        var nestePersonFraDatabasen = hentNestePerson(ringer, "AND hypersysID is not null ")
        if (nestePersonFraDatabasen != null) {
            return nestePersonFraDatabasen
        }

        hentMedlemmerFraLokallag(userId, jwt)

        nestePersonFraDatabasen = hentNestePerson(ringer, "AND hypersysID is not null ")
        if (nestePersonFraDatabasen != null) {
            return nestePersonFraDatabasen
        }

        return null
    }

    private fun hentMedlemmerFraLokallag(userId: UserId, jwt: JsonWebToken) =
        hypersysService.getMedlemmer(userId, jwt)
            .filter { medlem -> personRepository.find("hypersysID", medlem["member_id"]).count() == 0L }
            .map { modelConverter.convertMembershipToPerson(it) }
            .forEach { personRepository.save(it) }

    fun getPerson(userId: UserId): Person = personRepository.find("hypersysID", userId.userId).firstResult()

    private fun hentNestePerson(ringer: Person, hypersysQuery: String) = databaseUpdater.getResultList(
        "SELECT v.id FROM v_personerSomKanRinges v " +
                "WHERE fylke = ${ringer.fylke} " +
                hypersysQuery +
                " ORDER BY ABS(lokallag-'${ringer.lokallag}') ASC, " +
                "v.hypersysID DESC")
        .firstOrNull()



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



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
        samtaleRepository.persist(
            PersistentSamtale(
                ringt = request.ringtID.toInt(),
                ringer = hypersysIDTilRingerId(autentisertRequest.userId).toString().toInt(),
                resultat = request.resultat.nr,
                kommentar = request.kommentar
            ))
        lagreResultat(getNesteGroupID(request), request)
    }

    private fun getNesteGroupID(request: ResultatFraSamtaleRequest): GroupID? {
        return when {
            request.resultat.nesteGroupID != null -> request.resultat.nesteGroupID
            erFleireEnnToIkkeSvar(request) -> GroupID.Ferdigringt
            else -> null
        }
    }

    private fun lagreResultat(nesteGroupID: GroupID?, request: ResultatFraSamtaleRequest) {
        nesteGroupID?.nr?.let { personRepository.update("groupID=?1 where id=?2", it, request.ringtID) }
        if (request.skalRegistrere()) {
            registrerKoronaspesifikkeResultat(request)
        }
    }

    override fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): NestePersonAaRingeResponse {
        request.validate()
        val oppringtNummer = request.ringtNummer().replace("+47", "")
        val personSomRingerTilbake: Person = personRepository.find("telefonnummer", oppringtNummer).firstResult()

        startSamtale(
            AutentisertStartSamtaleRequest(
                userId = request.userId,
                startSamtaleRequest = StartSamtaleRequest(
                    skalRingesID = personSomRingerTilbake.id
                ),
                modus = Modus.velgere
            ))
        return personSomRingerTilbake.let { NestePersonAaRingeResponse(person = it, tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1"))}
    }

    private fun erFleireEnnToIkkeSvar(request: ResultatFraSamtaleRequest): Boolean {
        val resultat: List<Int> = samtaleRepository.find("where ringt=?1 and resultat=0", request.ringtID).list<PersistentSamtale>().map { it.resultat }
        val fleireEnnToIkkeSvar: Boolean = resultat.filter { it == 0 }.count() > 2
        val ingenSvar: Boolean = resultat.filter { it != 0 && it != 9 }.count() == 0
        return ingenSvar && fleireEnnToIkkeSvar && request.resultat == Resultat.Ikke_svar
    }

    private fun registrerKoronaspesifikkeResultat(request: ResultatFraSamtaleRequest) {
        val resultat = request.modusspesifikkeResultat as KoronaspesifikkeResultat

        oppfoelgingKoronaRepository.persist(OppfoelgingKorona(
            personId = request.ringtID.toInt(),
            koronaprogram = resultat.vilHaKoronaprogram,
            merAktiv = resultat.vilBliMerAktiv,
            valgkampsbrev = resultat.vilHaValgkampsbrev,
            vilIkkeBliRingt = request.vilIkkeBliRingt
        ))
    }

    fun hypersysIDTilRingerId(userId: UserId) =
        databaseUpdater.getResultList(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).first()
}