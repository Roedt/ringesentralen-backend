package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.KoronaspesifikkeResultat
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
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
    val persistentSamtaleRepository: PersistentSamtaleRepository,
    val oppfoelgingKoronaRepository: OppfoelgingKoronaRepository,
    val hypersysService: HypersysService
): RingService {

    override fun hentNestePersonAaRinge(request: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse? =
        hentFoerstePerson(request)
            ?.let { it as Int }
            ?.let { personRepository.findById(it.toLong()) }
            ?.let { NestePersonAaRingeResponse(person = it, tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1"))}
            ?.also { oppslagRepository.persist(Oppslag(ringt = it.person.id.toInt(), ringerHypersysId = request.userId() )) }

    private fun hentFoerstePerson(request: AutentisertNestePersonAaRingeRequest): Any? {
        if (request.modus == Modus.Velger) {
            val ringer = getPerson(request.userId)
            return databaseUpdater.getResultList("SELECT v.id FROM v_personerSomKanRinges v " +
                    "WHERE fylke = ${ringer.fylke} " +
                    "ORDER BY ABS(lokallag-'${ringer.lokallag}') ASC")
                .firstOrNull()
        }
        return -1
    }

    fun getPerson(userId: UserId) = personRepository.find("hypersysID", userId.userId).firstResult<Person>()

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

        persistentSamtaleRepository.persist(
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
        persistentSamtaleRepository.persist(
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
                modus = Modus.Velger
            ))
        return personSomRingerTilbake.let { NestePersonAaRingeResponse(person = it, tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1"))}
    }

    private fun erFleireEnnToIkkeSvar(request: ResultatFraSamtaleRequest): Boolean {
        val resultat: List<Int>? = databaseUpdater.getResultList("select resultat from `samtale` where ringt = ${request.ringtID} and resultat = 0")?.map { it as Int }
        val fleireEnnToIkkeSvar: Boolean = (resultat?.filter { it == 0 }?.count() ?: 0) > 2
        val ingenSvar: Boolean = (resultat?.filter { it != 0 && it != 9 }?.count() ?: 0) == 0
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