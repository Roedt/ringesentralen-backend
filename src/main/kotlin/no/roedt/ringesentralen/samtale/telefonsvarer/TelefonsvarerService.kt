package no.roedt.ringesentralen.samtale.telefonsvarer

import no.roedt.ringesentralen.hypersys.Ringer
import no.roedt.ringesentralen.hypersys.RingerRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
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
        val person = personRepository.find("telefonnummer", request.request.telefonnummer).firstResult<Person>() ?: return

        val systembruker = personRepository.find("fornavn='Systembruker' and etternavn='Frontend'").firstResult<Person>()

        samtaleRepository.persist(PersistentSamtale(
            ringt = person.id.toInt(),
            ringer = ringerRepository.find("personId", systembruker.id.toInt()).firstResult<Ringer>().id.toInt(),
            kommentar = "Resultat frå telefonsvar",
            resultat = request.resultat().nr
        ))

        if (GroupID.referencesOneOf(person.groupID, GroupID.Admin, GroupID.LokalGodkjenner, GroupID.GodkjentRinger, GroupID.UgodkjentRinger)) return
        request.resultat().nesteGroupID?.nr?.let { personRepository.update("groupID=?1 where id=?2", it, person.id) }
    }

}
