package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.PersonRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager

interface BrukereService {
    fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring
    fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring
}

@ApplicationScoped
class BrukereServiceBean(
        val personRepository: PersonRepository,
        val entityManager: EntityManager
): BrukereService {

    override fun godkjennRinger(godkjennRequest: TilgangsendringsRequest): Brukerendring {
        val godkjennerPhone: String = personRepository.findById(godkjennRequest.utfoerende).phone
        val godkjentPhone: String = personRepository.findById(godkjennRequest.utfoerende).phone
        entityManager.createNativeQuery("CALL sp_godkjennBruker(${godkjennerPhone},${godkjentPhone}, 6)")
        return Brukerendring(personID = godkjennRequest.personMedEndraTilgang, nyGroupId = 6)
    }


    override fun avslaaRinger(avslaaRequest: TilgangsendringsRequest): Brukerendring {
        val utfoerendePHone: String = personRepository.findById(avslaaRequest.utfoerende).phone
        val avslaattPhone: String = personRepository.findById(avslaaRequest.utfoerende).phone
        entityManager.createNativeQuery("CALL sp_godkjennBruker(${utfoerendePHone},${avslaattPhone}, 5)")
        return Brukerendring(personID = avslaaRequest.personMedEndraTilgang, nyGroupId = 5)
    }
}