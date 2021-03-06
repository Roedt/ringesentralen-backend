package no.roedt.ringesentralen.samtale.telefonsvarer

import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.Ringer
import no.roedt.ringesentralen.person.RingerRepository
import no.roedt.ringesentralen.samtale.PersistentSamtale
import no.roedt.ringesentralen.samtale.PersistentSamtaleRepository
import javax.enterprise.context.Dependent

@Dependent
class TelefonsvarerService(
    private val personRepository: PersonRepository,
    private val samtaleRepository: PersistentSamtaleRepository,
    private val ringerRepository: RingerRepository
) {
    fun postSvarFraTelefonsvarer(request: AutentisertTelefonsvarerRequest) {
        val optionalPerson = personRepository.find("telefonnummer", request.request.telefonnummer).firstResultOptional<Person>()
        if (optionalPerson.isEmpty) {
            println("${request.request.telefonnummer} ringte og svarte ja til meir informasjon uten å være registrert i systemet")
            return
        }
        val person = optionalPerson.get()

        val systembruker = personRepository.find("fornavn='Systembruker' and etternavn='Frontend'").firstResult<Person>()

        samtaleRepository.persist(
            PersistentSamtale(
                ringt = person.id.toInt(),
                ringer = ringerRepository.find("personId", systembruker.id.toInt()).firstResult<Ringer>().id.toInt(),
                kommentar = "Resultat frå telefonsvar",
                resultat = request.resultat().nr,
                modus = if (person.hypersysID != null) Modus.medlemmer else Modus.velgere
            )
        )

        if (GroupID.isBrukerEllerVenter(person.groupID())) return
        request.resultat().nesteGroupID?.nr?.let { personRepository.update("groupID=?1 where id=?2", it, person.id) }
    }
}
