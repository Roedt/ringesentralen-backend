package no.roedt.ringesentralen.brukere

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.ForbiddenException
import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.Brukerendring
import no.roedt.brukere.Godkjenning
import no.roedt.brukere.GodkjenningService
import no.roedt.brukere.GroupID
import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.konvertering.ModelConverter
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.UserId
import no.roedt.postnummer.Postnummer

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
    val personService: PersonService,
    val epostSender: RingesentralenEpostformulerer,
    val hypersysService: HypersysService,
    val godkjenningService: GodkjenningService,
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

    private fun endreTilgang(
        request: AutentisertTilgangsendringRequest,
        nyTilgang: GroupID
    ): Brukerendring {
        assertAutorisert(request)

        val personMedEndraTilgang = request.personMedEndraTilgang()

        val ringerId = personService.hypersysIDTilRingerId(request.userId)
        godkjenningService.persist(
            Godkjenning(
                godkjenner = ringerId,
                godkjentPerson = personMedEndraTilgang,
                nyGroupId = nyTilgang.nr
            )
        )
        personService.oppdaterRolle(nyTilgang.nr, personMedEndraTilgang)
        val brukerendring = Brukerendring(personID = personMedEndraTilgang, nyGroupId = nyTilgang, epostSendt = false)

        val person = personService.findById(personMedEndraTilgang)
        oppdaterNavnFraHypersys(person.postnummer, person.hypersysID)

        sendEpost(person, nyTilgang, brukerendring)
        return brukerendring
    }

    private fun sendEpost(
        person: Person,
        nyTilgang: GroupID,
        brukerendring: Brukerendring
    ) {
        try {
            epostSender.sendEpostOmEndraStatus(person, nyTilgang)
            brukerendring.epostSendt = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun oppdaterNavnFraHypersys(
        naavaerendePostnummer: Postnummer,
        hypersysID: Int?
    ) = hypersysService.hentFraMedlemslista(hypersysID)?.let {
        val nyttPostnr = modelConverter.finnPostnummer(it).takeIf { i -> !i.erUkjent() } ?: naavaerendePostnummer
        personService.oppdaterNavnFraHypersys(
            it.first_name,
            it.last_name,
            nyttPostnr,
            hypersysID
        )
    }

    private fun assertAutorisert(request: AutentisertTilgangsendringRequest) {
        val ringersBrukertype = hypersysIdTilPerson(request.userId).groupID()
        val personMedEndraTilgang = personService.findById(request.personMedEndraTilgang())
        val groupID = personMedEndraTilgang.groupID()

        if (personMedEndraTilgang.isSystembruker()) throw ForbiddenException("Kan ikkje endre systembruker")
        if (RingesentralenGroupID.Admin.references(groupID)) throw ForbiddenException("Kan ikkje endre admins")
        if (RingesentralenGroupID.LokalGodkjenner.references(ringersBrukertype) &&
            RingesentralenGroupID.LokalGodkjenner.references(
                groupID
            )
        ) {
            throw ForbiddenException(
                "Godkjennere kan ikkje endre andre godjennere"
            )
        }
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) = personService.finnFraHypersysId(hypersysId.userId).firstResult<Person>()
}
