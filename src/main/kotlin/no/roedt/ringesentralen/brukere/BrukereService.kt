package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.PersonRepository
import no.roedt.ringesentralen.samtale.GroupID
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface BrukereService {
    fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring
    fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring
    fun reaktiverRinger(reaktiverRequest: TilgangsendringsRequest): Brukerendring
    fun deaktiverRinger(deaktiverRequest: TilgangsendringsRequest): Brukerendring
    fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring
    fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring
}

@ApplicationScoped
class BrukereServiceBean(
        val personRepository: PersonRepository,
        val entityManager: EntityManager
): BrukereService {

    override fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(godkjennRequest, GroupID.GodkjentRinger)

    override fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(avslaaRequest, GroupID.AvslaattRinger)

    override fun reaktiverRinger(reaktiverRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(reaktiverRequest, GroupID.GodkjentRinger)

    override fun deaktiverRinger(deaktiverRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(deaktiverRequest, GroupID.AvslaattRinger)

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(tilLokalGodkjennerRequest, GroupID.LokalGodkjenner)

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring = endreTilgang(fjernSomLokalGodkjennerRequest, GroupID.GodkjentRinger)

    private fun endreTilgang(request: TilgangsendringsRequest, nyTilgang: GroupID): Brukerendring {
        entityManager.createNativeQuery("CALL sp_godkjennBruker(${getPhone(request.utfoerende)}, ${getPhone(request.personMedEndraTilgang)}, ${nyTilgang.nr})").resultList
        return Brukerendring(personID = request.personMedEndraTilgang, nyGroupId = nyTilgang)
    }

    private fun getPhone(personID: Long) = personRepository.findById(personID).phone
}