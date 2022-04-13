package no.roedt.ringesentralen.brukere

import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.Brukerendring
import no.roedt.brukere.Godkjenning
import no.roedt.brukere.GodkjenningRepository
import no.roedt.brukere.GroupID
import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.ModelConverter
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.RingesentralenGroupID
import no.roedt.person.UserId
import no.roedt.ringesentralen.DatabaseUpdater
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.ForbiddenException

interface TilgangsendringService {
    fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun giTilgangTilAaRingeMedlemmer(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun fjernTilgangTilAaRingeMedlemmer(request: AutentisertTilgangsendringRequest): Brukerendring
}

@ApplicationScoped
class TilgangsendringServiceBean(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val epostSender: RingesentralenEpostformulerer,
    val hypersysService: HypersysService,
    val godkjenningRepository: GodkjenningRepository,
    val modelConverter: ModelConverter
) : TilgangsendringService {

    override fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring =
        endreTilgang(godkjennRequest, RingesentralenGroupID.GodkjentRinger)

    override fun giTilgangTilAaRingeMedlemmer(godkjennRequest: AutentisertTilgangsendringRequest) =
        endreTilgang(godkjennRequest, RingesentralenGroupID.GodkjentRingerMedlemmer)

    override fun fjernTilgangTilAaRingeMedlemmer(request: AutentisertTilgangsendringRequest) =
        endreTilgang(request, RingesentralenGroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring =
        endreTilgang(deaktiverRequest, RingesentralenGroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring =
        endreTilgang(tilLokalGodkjennerRequest, RingesentralenGroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring =
        endreTilgang(fjernSomLokalGodkjennerRequest, RingesentralenGroupID.GodkjentRingerMedlemmer)

    private fun endreTilgang(request: AutentisertTilgangsendringRequest, nyTilgang: GroupID): Brukerendring {
        assertAutorisert(request)

        val personMedEndraTilgang = request.personMedEndraTilgang()

        val ringerId = hypersysIDTilRingerId(request.userId)
        godkjenningRepository.persist(
            Godkjenning(
                godkjenner = ringerId.toString().toInt(),
                godkjentPerson = personMedEndraTilgang,
                nyGroupId = nyTilgang.nr
            )
        )
        personRepository.update("groupID=?1 where id=?2", nyTilgang.nr, personMedEndraTilgang)
        val brukerendring = Brukerendring(personID = personMedEndraTilgang, nyGroupId = nyTilgang, epostSendt = false)

        val person = personRepository.findById(personMedEndraTilgang)
        oppdaterNavnFraHypersys(person.postnummer, person.hypersysID)

        sendEpost(person, nyTilgang, brukerendring)
        return brukerendring
    }

    private fun sendEpost(person: Person, nyTilgang: GroupID, brukerendring: Brukerendring) {
        try {
            epostSender.sendEpostOmEndraStatus(person, nyTilgang)
            brukerendring.epostSendt = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun oppdaterNavnFraHypersys(naavaerendePostnummer: Int, hypersysID: Int?) =
        hypersysService.hentFraMedlemslista(hypersysID)?.let {
            val nyttPostnr = modelConverter.finnPostnummer(it).takeIf { i -> i > -1 } ?: naavaerendePostnummer
            personRepository.update(
                "fornavn = ?1, etternavn = ?2, postnummer = ?3 where hypersysID = ?4",
                it["first_name"],
                it["last_name"],
                nyttPostnr,
                hypersysID
            )
        }

    private fun assertAutorisert(request: AutentisertTilgangsendringRequest) {
        val ringersBrukertype = hypersysIdTilPerson(request.userId).groupID()
        val personMedEndraTilgang = personRepository.findById(request.personMedEndraTilgang())
        val groupID = personMedEndraTilgang.groupID()

        if (personMedEndraTilgang.isSystembruker()) throw ForbiddenException("Kan ikkje endre systembruker")
        if (RingesentralenGroupID.Admin.references(groupID)) throw ForbiddenException("Kan ikkje endre admins")
        if (RingesentralenGroupID.LokalGodkjenner.references(ringersBrukertype) && RingesentralenGroupID.LokalGodkjenner.references(groupID)) throw ForbiddenException(
            "Godkjennere kan ikkje endre andre godjennere"
        )
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) =
        personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()

    private fun hypersysIDTilRingerId(userId: UserId) = databaseUpdater.getResultList(
        "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
    ).first()
}
