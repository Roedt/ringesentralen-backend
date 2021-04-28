package no.roedt.ringesentralen.verving

import no.roedt.ringesentralen.Kilde
import no.roedt.ringesentralen.brukere.FylkeRepository
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import javax.enterprise.context.Dependent

@Dependent
class VervingService(
    private val personRepository: PersonRepository,
    private val vervingRepository: VervingRepository,
    private val lokallagRepository: LokallagRepository,
    private val fylkeRepository: FylkeRepository
) {

    fun postPersonSomSkalRinges(request: AutentisertVervingRequest) : Pair<Boolean, Person> {
        val postnummer = request.request.postnummer
        vervingRepository.persist(Verving(
            telefonnummer = request.request.telefonnummer,
            fornavn = request.request.fornavn,
            etternavn = request.request.etternavn,
            postnummer = request.request.postnummer,
            verversNavn = request.request.verversNavn
        ))

        val vervaFraFoer = personRepository.find("telefonnummer=?1", request.request.telefonnummer).singleResultOptional<Person>()
        if (vervaFraFoer.isPresent) return Pair(false, vervaFraFoer.get())

        val person = Person(
            hypersysID = null,
            fornavn = request.request.fornavn,
            etternavn = request.request.etternavn,
            telefonnummer = request.request.telefonnummer,
            email = null,
            postnummer = postnummer,
            fylke = fylkeRepository.toFylke(postnummer),
            lokallag = lokallagRepository.fromPostnummer(postnummer),
            groupID = GroupID.ManglerSamtykke.nr,
            kilde = Kilde.Verva,
            iperID = null
        )
        personRepository.persist(person)
        return Pair(true, person)
    }

    fun mottaSvar(request: AutentisertMottaSvarRequest) {
        val nextValue = if (request.request.svar) GroupID.PrioritertAaRinge else GroupID.Slett
        personRepository.update("groupID=?1 where telefonnummer=?2", nextValue.nr, request.request.telefonnummer)
    }
}