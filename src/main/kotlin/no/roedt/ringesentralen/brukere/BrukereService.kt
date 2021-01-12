package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.*
import no.roedt.ringesentralen.samtale.GroupID
import no.roedt.ringesentralen.samtale.RingbarPerson
import javax.enterprise.context.ApplicationScoped

interface BrukereService {
    fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring
    fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring
    fun reaktiverRinger(reaktiverRequest: TilgangsendringsRequest): Brukerendring
    fun deaktiverRinger(deaktiverRequest: TilgangsendringsRequest): Brukerendring
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring
    fun hentBrukarar(hentBrukararRequest: HentBrukararRequest): List<Brukarinformasjon>
}

@ApplicationScoped
class BrukereServiceBean(
        val personRepository: PersonRepository,
        val databaseUpdater: DatabaseUpdater
): BrukereService {

    override fun hentBrukarar(hentBrukararRequest: HentBrukararRequest): List<Brukarinformasjon> =
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

    override fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(godkjennRequest, GroupID.GodkjentRinger)

    override fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(avslaaRequest, GroupID.AvslaattRinger)

    override fun reaktiverRinger(reaktiverRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(reaktiverRequest, GroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(deaktiverRequest, GroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(tilLokalGodkjennerRequest, GroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(fjernSomLokalGodkjennerRequest, GroupID.GodkjentRinger)

    private fun endreTilgang(request: TilgangsendringsRequest, nyTilgang: GroupID): Brukerendring {
        databaseUpdater.update("CALL sp_godkjennBruker(${getPhone(request.utfoerende)}, ${getPhone(request.personMedEndraTilgang)}, ${nyTilgang.nr})")
        return Brukerendring(personID = request.personMedEndraTilgang, nyGroupId = nyTilgang)
    }

    private fun getPhone(personID: Long) = personRepository.findById(personID).phone
}