package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.NotAuthorizedException

interface BrukereService {
    fun godkjennRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun avslaaRinger(avslaaRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun reaktiverRinger(reaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring
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
    val lokallagRepository: LokallagRepository
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
                    rolle = GroupID.getRoller(r.groupID)
                )
            }

    override fun godkjennRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(godkjennRequest, GroupID.GodkjentRinger)

    override fun avslaaRinger(avslaaRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(avslaaRequest, GroupID.AvslaattRinger)

    override fun reaktiverRinger(reaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(reaktiverRequest, GroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(deaktiverRequest, GroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(tilLokalGodkjennerRequest, GroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(fjernSomLokalGodkjennerRequest, GroupID.GodkjentRinger)

    private fun endreTilgang(request: AutentisertTilgangsendringRequest, nyTilgang: GroupID): Brukerendring {
        assertAutorisert(request)

        val personMedEndraTilgang = request.personMedEndraTilgang()

        val ringerId = hypersysIDTilRingerId(request.userId)
        databaseUpdater.update("CALL sp_godkjennBruker(${ringerId}, ${personMedEndraTilgang}, ${nyTilgang.nr})")
        return Brukerendring(personID = personMedEndraTilgang, nyGroupId = nyTilgang)
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