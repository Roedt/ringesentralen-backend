package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.PersonRepository
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

    override fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring {
        endreTilgang(godkjennRequest, 6)
        return Brukerendring(personID = godkjennRequest.personMedEndraTilgang, nyGroupId = 6)
    }

    override fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring {
        endreTilgang(avslaaRequest, 5)
        return Brukerendring(personID = avslaaRequest.personMedEndraTilgang, nyGroupId = 5)
    }

    override fun reaktiverRinger(reaktiverRequest: TilgangsendringsRequest): Brukerendring {
        endreTilgang(reaktiverRequest, 6)
        return Brukerendring(personID = reaktiverRequest.personMedEndraTilgang, nyGroupId = 6)
    }

    override fun deaktiverRinger(deaktiverRequest: TilgangsendringsRequest): Brukerendring {
        endreTilgang(deaktiverRequest, 5)
        return Brukerendring(personID = deaktiverRequest.personMedEndraTilgang, nyGroupId = 5)
    }

    override fun gjoerRingerTilLokalGodkjenner(tilLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring {
        endreTilgang(tilLokalGodkjennerRequest, 8)
        return Brukerendring(personID = tilLokalGodkjennerRequest.personMedEndraTilgang, nyGroupId = 8)
    }

    override fun fjernRingerSomLokalGodkjenner(fjernSomLokalGodkjennerRequest: TilgangsendringsRequest): Brukerendring {
        endreTilgang(fjernSomLokalGodkjennerRequest, 6)
        return Brukerendring(personID = fjernSomLokalGodkjennerRequest.personMedEndraTilgang, nyGroupId = 6)
    }

    private fun endreTilgang(request: TilgangsendringsRequest, nyTilgang: Int) =
        entityManager.createNativeQuery("CALL sp_godkjennBruker(${getPhone(request.utfoerende)}, ${getPhone(request.personMedEndraTilgang)}, ${nyTilgang})").resultList

    private fun getPhone(personID: Long) = personRepository.findById(personID).phone
}