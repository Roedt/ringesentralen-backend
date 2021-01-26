package no.roedt.ringesentralen.brukere

import UserId
import no.roedt.ringesentralen.*
import no.roedt.ringesentralen.samtale.GroupID
import no.roedt.ringesentralen.samtale.RingbarPerson
import javax.enterprise.context.ApplicationScoped

interface BrukereService {
    fun godkjennRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun avslaaRinger(avslaaRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun reaktiverRinger(reaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring
    fun hentBrukarar(): List<Brukarinformasjon>
}

@ApplicationScoped
class BrukereServiceBean(
        val personRepository: PersonRepository,
        val databaseUpdater: DatabaseUpdater
): BrukereService {

    override fun hentBrukarar(): List<Brukarinformasjon> =
        personRepository.find("groupID >= ${GroupID.GodkjentRinger.nr}")
            .list<RingbarPerson>()
            .map { r ->
                Brukarinformasjon(
                    fornamn = r.givenName,
                    etternamn = r.familyName,
                    telefonnummer = Telefonnummer(nummer = r.phone.toInt()),
                    postnummer = Postnummer(r.postnumber.padStart(4, '0')),
                    fylke = Fylke.from(r.countyID),
                    epost = r.email ?: "",
                    hypersysID = r.hypersysID ?: -1
                )
            }

    override fun godkjennRinger(godkjennRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(godkjennRequest, GroupID.GodkjentRinger)

    override fun avslaaRinger(avslaaRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(avslaaRequest, GroupID.AvslaattRinger)

    override fun reaktiverRinger(reaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(reaktiverRequest, GroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(deaktiverRequest, GroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(tilLokalGodkjennerRequest, GroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: AutentisertTilgangsendringRequest): Brukerendring = endreTilgang(fjernSomLokalGodkjennerRequest, GroupID.GodkjentRinger)

    private fun endreTilgang(request: AutentisertTilgangsendringRequest, nyTilgang: GroupID): Brukerendring {
        databaseUpdater.update("CALL sp_godkjennBruker(${hypersysIdTilTelefonnummer(request.userId)}, ${getPhone(request.personMedEndraTilgang())}, ${nyTilgang.nr})")
        return Brukerendring(personID = request.personMedEndraTilgang(), nyGroupId = nyTilgang)
    }

    private fun getPhone(personID: Long) = personRepository.findById(personID).phone

    private fun hypersysIdTilTelefonnummer(hypersysId: UserId) = personRepository.find("hypersysID", hypersysId.userId).firstResult<RingbarPerson>().phone
}