package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Roles
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.ForbiddenException

interface BrukereService {
    fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun getBrukere(userId: UserId, groups: Set<String>): List<Brukerinformasjon>
}

@ApplicationScoped
class BrukereServiceBean(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val fylkeRepository: FylkeRepository,
    val lokallagRepository: LokallagRepository,
    val epostSender: EpostSender,
    val hypersysService: HypersysService,
    val godkjenningRepository: GodkjenningRepository
): BrukereService {

    override fun getBrukere(userId: UserId, groups: Set<String>): List<Brukerinformasjon> {
        val brukersFylke = personRepository.find("hypersysID", userId.userId).firstResult<Person>().fylke
        val filtrerPaaFylke = if (groups.contains(Roles.admin)) "" else "and fylke=$brukersFylke"
        return personRepository.find("groupID >= ${GroupID.UgodkjentRinger.nr}" + filtrerPaaFylke)
            .list<Person>()
            .map { r ->
                Brukerinformasjon(
                    id = r.id,
                    fornavn = r.fornavn,
                    etternavn = r.etternavn,
                    telefonnummer = Telefonnummer(nummer = r.telefonnummer ?: ""),
                    postnummer = r.postnummer,
                    fylke = fylkeRepository.findById(r.fylke),
                    epost = r.email ?: "",
                    hypersysID = r.hypersysID ?: -1,
                    lokallag = lokallagRepository.findById(r.lokallag.toLong()),
                    rolle = GroupID.from(r.groupID).roller
                )
            }
    }

    override fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(godkjennRequest, GroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(deaktiverRequest, GroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(tilLokalGodkjennerRequest, GroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(fjernSomLokalGodkjennerRequest, GroupID.GodkjentRinger)

    private fun endreTilgang(request: AutentisertTilgangsendringRequest, nyTilgang: GroupID): Brukerendring {
        assertAutorisert(request)

        val personMedEndraTilgang = request.personMedEndraTilgang()

        val ringerId = hypersysIDTilRingerId(request.userId)
        godkjenningRepository.persist(Godkjenning(godkjenner = ringerId.toString().toInt(), godkjentPerson = personMedEndraTilgang.toInt(), nyGroupId = nyTilgang.nr))
        personRepository.update("groupID=?1 where id=?2", nyTilgang.nr, personMedEndraTilgang)
        val brukerendring = Brukerendring(personID = personMedEndraTilgang, nyGroupId = nyTilgang, epostSendt = false)

        val person = personRepository.findById(personMedEndraTilgang)
        oppdaterNavnFraHypersys(request, person.hypersysID)

        try {
            epostSender.sendEpost(person, nyTilgang)
            brukerendring.epostSendt=true
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return brukerendring
    }

    private fun oppdaterNavnFraHypersys(request: AutentisertTilgangsendringRequest, hypersysID: Int?) {
        hypersysID
            ?.let { UserId(userId = it) }
            ?.let { hypersysService.getLokallag(userId = it)}
            ?.let { hypersysService.getMedlemmer(it, request.jwt) }
            ?.firstOrNull { it["member_id"] == hypersysID }
            ?.let {
                personRepository.update("fornavn = ?1, etternavn = ?2 where hypersysID = ?3", it["first_name"], it["last_name"], hypersysID)
            }
    }

    private fun assertAutorisert(request: AutentisertTilgangsendringRequest) {
        val ringersBrukertype = hypersysIdTilPerson(request.userId).groupID
        val groupID = personRepository.findById(request.personMedEndraTilgang()).groupID

        if (GroupID.Admin.references(groupID)) {
            throw ForbiddenException("Kan ikkje endre admins")
        }
        if (GroupID.LokalGodkjenner.references(ringersBrukertype) && GroupID.LokalGodkjenner.references(groupID)) {
            throw ForbiddenException("Godkjennere kan ikkje endre andre godjennere")
        }
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) =
        personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()


    private fun hypersysIDTilRingerId(userId: UserId) =
        databaseUpdater.getResultList(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).first()
}