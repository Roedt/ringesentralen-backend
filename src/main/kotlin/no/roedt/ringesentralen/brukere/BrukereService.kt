package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.NotAuthorizedException

interface BrukereService {
    fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun getBrukere(): List<Brukerinformasjon>
}

@ApplicationScoped
class BrukereServiceBean(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val fylkeRepository: FylkeRepository,
    val lokallagRepository: LokallagRepository,
    val epostSender: EpostSender,
    val hypersysService: HypersysService
): BrukereService {

    override fun getBrukere(): List<Brukerinformasjon> =
        personRepository.find("groupID >= ${GroupID.GodkjentRinger.nr}")
            .list<Person>()
            .map { r ->
                Brukerinformasjon(
                    id = r.id,
                    fornavn = r.fornavn,
                    etternavn = r.etternavn,
                    telefonnummer = Telefonnummer(nummer = r.telefonnummer?.toInt() ?: -1),
                    postnummer = r.postnummer,
                    fylke = fylkeRepository.findById(r.fylke),
                    epost = r.email ?: "",
                    hypersysID = r.hypersysID ?: -1,
                    lokallag = lokallagRepository.findById(r.lokallag.toLong()),
                    rolle = GroupID.from(r.groupID).roller
                )
            }

    override fun aktiverRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(godkjennRequest, GroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(deaktiverRequest, GroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(tilLokalGodkjennerRequest, GroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(fjernSomLokalGodkjennerRequest, GroupID.GodkjentRinger)

    private fun endreTilgang(request: AutentisertTilgangsendringRequest, nyTilgang: GroupID): Brukerendring {
        assertAutorisert(request)

        val personMedEndraTilgang = request.personMedEndraTilgang()

        val ringerId = hypersysIDTilRingerId(request.userId)
        databaseUpdater.updateNoTran("CALL sp_godkjennBruker(${ringerId}, ${personMedEndraTilgang}, ${nyTilgang.nr})")
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
            ?.let { hypersysService.getMedlemmer(it, request.jwt) }
            ?.firstOrNull { it["member_id"] == hypersysID }
            ?.let {
                personRepository.update("fornavn = ?1, etternavn = ?2 where hypersysID = ?3", it["first_name"], it["last_name"], hypersysID)
            }
    }

    private fun assertAutorisert(request: AutentisertTilgangsendringRequest) {
        val ringersBrukertype = hypersysIdTilPerson(request.userId).groupID
        val groupID = personRepository.findById(request.personMedEndraTilgang()).groupID
        if (!GroupID.Admin.references(ringersBrukertype) && GroupID.Admin.references(groupID)) {
            throw NotAuthorizedException("Godkjennere kan ikkje endre admins")
        }
        if (GroupID.LokalGodkjenner.references(ringersBrukertype) && GroupID.LokalGodkjenner.references(groupID)) {
            throw NotAuthorizedException("Godkjennere kan ikkje endre andre godjennere")
        }
    }

    private fun hypersysIdTilPerson(hypersysId: UserId) =
        personRepository.find("hypersysID", hypersysId.userId).firstResult<Person>()


    private fun hypersysIDTilRingerId(userId: UserId) =
        databaseUpdater.getResultList(
            "select ringer.id from ringer inner join person on person.id = ringer.personId and person.hypersysID = ${userId.userId} "
        ).first()
}