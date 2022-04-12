package no.roedt.ringesentralen.samtale

import no.roedt.lokallag.LokallagRepository
import no.roedt.person.GroupID
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.UserId
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.person.Ringer
import no.roedt.ringesentralen.person.RingerRepository
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Valg21SpesifikkeResultat
import no.roedt.ringesentralen.samtale.start.AutentisertNestePersonAaRingeRequest
import no.roedt.ringesentralen.samtale.start.AutentisertRingerTilbakeRequest
import no.roedt.ringesentralen.samtale.start.AutentisertStartSamtaleRequest
import no.roedt.ringesentralen.samtale.start.NestePersonAaRingeFinder
import no.roedt.ringesentralen.samtale.start.NestePersonAaRingeResponse
import no.roedt.ringesentralen.samtale.start.StartSamtaleRequest
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
    val samtaleRepository: PersistentSamtaleRepository,
    val oppfoelgingValg21Repository: OppfoelgingValg21Repository,
    val lokallagRepository: LokallagRepository,
    val ringerRepository: RingerRepository,
    val nestePersonAaRingeFinder: NestePersonAaRingeFinder
) : RingService {

    override fun hentNestePersonAaRinge(request: AutentisertNestePersonAaRingeRequest) = nestePersonAaRingeFinder.hentNestePersonAaRinge(request)

    override fun startSamtale(request: AutentisertStartSamtaleRequest) = samtaleRepository.persist(
        PersistentSamtale(
            ringt = request.skalRingesID().toInt(),
            ringer = hypersysIDTilRingerId(request.userId).toString().toInt(),
            resultat = Resultat.Samtale_startet.nr,
            ringesesjon = Ringesesjon.Ringing2022.id,
            kommentar = "Starter samtale",
            modus = request.modus
        )
    )

    override fun registrerResultatFraSamtale(autentisertRequest: AutentisertResultatFraSamtaleRequest) {
        val request = autentisertRequest.request
        assert(request.isGyldigResultat())
        val ringer = hypersysIDTilRingerId(autentisertRequest.userId).toString().toInt()
        val persistentSamtale = PersistentSamtale(
            ringt = request.ringtID.toInt(),
            ringer = ringer,
            resultat = request.resultat.nr,
            ringesesjon = Ringesesjon.Ringing2022.id,
            kommentar = request.kommentar,
            modus = autentisertRequest.modus
        )
        samtaleRepository.persist(persistentSamtale)
        lagreResultat(persistentSamtale.id.toLong(), getNesteGroupID(request), request)
    }

    private fun getNesteGroupID(request: ResultatFraSamtaleRequest): GroupID? = when {
        request.vilIkkeBliRingt -> GroupID.Ferdigringt
        request.resultat.nesteGroupID != null -> request.resultat.nesteGroupID
        erFleireEnnToIkkeSvar(request) -> GroupID.Ferdigringt
        else -> null
    }

    private fun lagreResultat(samtaleId: Long, nesteGroupID: GroupID?, request: ResultatFraSamtaleRequest) {
        if (request.skalRegistrere()) {
            registrerValg21SpesifikkeResultat(samtaleId, request)
        }
        if (isBrukerEllerVenterPaaGodkjenning(request.ringtID.toInt())) return
        nesteGroupID?.nr?.let { personRepository.update("groupID=?1 where id=?2", it, request.ringtID.toInt()) }
    }

    fun isBrukerEllerVenterPaaGodkjenning(ringer: Int) =
        GroupID.isBrukerEllerVenter(
            ringerRepository.find("personId=?1", ringer).singleResultOptional<Ringer>()
                .map { it.personId }
                .map { personRepository.findById(it) }
                .map { it.groupID() }
                .orElse(-1)
        )

    override fun noenRingerTilbake(request: AutentisertRingerTilbakeRequest): NestePersonAaRingeResponse {
        request.validate()
        val oppringtNummer = request.ringtNummer()
        var personSomRingerTilbake = personRepository.find("telefonnummer", oppringtNummer).firstResultOptional<Person>()
            .orElseGet { personRepository.find("telefonnummer", "-1").firstResult() }
        val modus = if (personSomRingerTilbake.hypersysID != null) Modus.medlemmer else Modus.velgere
        if (modus == Modus.medlemmer && !request.groups.contains(Roles.ringerMedlemmer)) {
            personSomRingerTilbake = personRepository.find("telefonnummer", "-1").firstResult()
        }

        startSamtale(
            AutentisertStartSamtaleRequest(
                userId = request.userId,
                startSamtaleRequest = StartSamtaleRequest(
                    skalRingesID = personSomRingerTilbake.id.toLong()
                ),
                modus = modus
            )
        )
        return toResponse(personSomRingerTilbake)
    }

    private fun toResponse(it: Person) =
        NestePersonAaRingeResponse(
            person = it,
            lokallagNavn = lokallagRepository.findById(it.lokallag).navn,
            tidlegareSamtalar = nestePersonAaRingeFinder.getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1")
        )

    private fun erFleireEnnToIkkeSvar(request: ResultatFraSamtaleRequest): Boolean {
        val resultat: List<Int> = samtaleRepository.list("ringt=?1 and resultat=${Resultat.Ikke_svar.nr}", request.ringtID.toInt()).map { it.resultat }.map { it }
        val fleireEnnToIkkeSvar: Boolean = resultat.filter { it == 0 }.count() > 2
        val ingenSvar: Boolean = resultat.filter { it != Resultat.Ikke_svar.nr && it != Resultat.Samtale_startet.nr }.count() == 0
        return ingenSvar && fleireEnnToIkkeSvar && request.resultat == Resultat.Ikke_svar
    }

    private fun registrerValg21SpesifikkeResultat(samtaleId: Long, request: ResultatFraSamtaleRequest) {
        val resultat = request.modusspesifikkeResultat as Valg21SpesifikkeResultat
        oppfoelgingValg21Repository.persist(
            OppfoelgingValg21(
                samtaleId = samtaleId.toInt(),
                vilBliMerAktiv = resultat.vilBliMerAktiv,
                vilPolitikkLink = resultat.vilPolitikkLink,
                vilIkkeBliRingt = request.vilIkkeBliRingt,
                vilHaMedlemsLink = resultat.vilHaMedlemsLink,
                vilHaFellesskapLink = resultat.vilHaFellesskapLink,
                vilBliRingtAugust = resultat.vilBliRingtAugust
            )
        )
    }

    fun hypersysIDTilRingerId(userId: UserId) =
        databaseUpdater.getResultList(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).first()
}
