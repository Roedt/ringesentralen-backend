package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.Ringer
import no.roedt.ringesentralen.person.RingerRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.ForbiddenException

interface BrukereService {
    fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun giTilgangTilAaRingeMedlemmer(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon>
    fun fjernTilgangTilAaRingeMedlemmer(request: AutentisertTilgangsendringRequest): Brukerendring
}

@ApplicationScoped
class BrukereServiceBean(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val fylkeRepository: FylkeRepository,
    val lokallagRepository: LokallagRepository,
    val epostSender: EpostSender,
    val hypersysService: HypersysService,
    val godkjenningRepository: GodkjenningRepository,
    val modelConverter: ModelConverter,
    val ringerRepository: RingerRepository
) : BrukereService {

    override fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon> {
        val brukersFylke = personRepository.find("hypersysID", request.userId.userId).firstResult<Person>().fylke
        val filtrerPaaFylke = if (request.groups.contains(Roles.admin)) "" else "and fylke=$brukersFylke"
        return personRepository.list(
            "(groupID=?1 or groupID=?2 or groupID=?3 or groupID=?4 or groupID=?5 or groupID=?6) $filtrerPaaFylke",
            GroupID.UgodkjentRinger.nr, GroupID.AvslaattRinger.nr, GroupID.GodkjentRinger.nr, GroupID.GodkjentRingerMedlemmer.nr, GroupID.LokalGodkjenner.nr, GroupID.Admin.nr
        )
            .filter { !it.isSystembruker() }
            .map(this::toBrukerinformasjon)
    }

    private fun toBrukerinformasjon(r: Person) = Brukerinformasjon(
        id = r.id,
        fornavn = r.fornavn,
        etternavn = r.etternavn,
        telefonnummer = r.telefonnummer,
        postnummer = r.postnummer,
        fylke = fylkeRepository.findById(r.fylke),
        epost = r.email ?: "",
        hypersysID = r.hypersysID ?: -1,
        lokallag = lokallagRepository.findById(r.lokallag.toLong()),
        rolle = GroupID.from(r.groupID()).roller,
        registreringstidspunkt = ringerRepository.find("personId", r.id.toInt()).firstResult<Ringer>().oppretta
    )

    override fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(godkjennRequest, GroupID.GodkjentRinger)

    override fun giTilgangTilAaRingeMedlemmer(godkjennRequest: AutentisertTilgangsendringRequest) = endreTilgang(godkjennRequest, GroupID.GodkjentRingerMedlemmer)

    override fun fjernTilgangTilAaRingeMedlemmer(request: AutentisertTilgangsendringRequest) = endreTilgang(request, GroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(deaktiverRequest, GroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(tilLokalGodkjennerRequest, GroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(fjernSomLokalGodkjennerRequest, GroupID.GodkjentRingerMedlemmer)

    private fun endreTilgang(request: AutentisertTilgangsendringRequest, nyTilgang: GroupID): Brukerendring {
        assertAutorisert(request)

        val personMedEndraTilgang = request.personMedEndraTilgang()

        val ringerId = hypersysIDTilRingerId(request.userId)
        godkjenningRepository.persist(Godkjenning(godkjenner = ringerId.toString().toInt(), godkjentPerson = personMedEndraTilgang.toInt(), nyGroupId = nyTilgang.nr))
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
            personRepository.update("fornavn = ?1, etternavn = ?2, postnummer = ?3 where hypersysID = ?4", it["first_name"], it["last_name"], nyttPostnr, hypersysID)
        }

    private fun assertAutorisert(request: AutentisertTilgangsendringRequest) {
        val ringersBrukertype = hypersysIdTilPerson(request.userId).groupID()
        val personMedEndraTilgang = personRepository.findById(request.personMedEndraTilgang())
        val groupID = personMedEndraTilgang.groupID()

        if (personMedEndraTilgang.isSystembruker()) throw ForbiddenException("Kan ikkje endre systembruker")
        if (GroupID.Admin.references(groupID)) throw ForbiddenException("Kan ikkje endre admins")
        if (GroupID.LokalGodkjenner.references(ringersBrukertype) && GroupID.LokalGodkjenner.references(groupID)) throw ForbiddenException("Godkjennere kan ikkje endre andre godjennere")
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) = personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()

    private fun hypersysIDTilRingerId(userId: UserId) = databaseUpdater.getResultList(
        "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
    ).first()
}
