package no.roedt.ringesentralen.verving

import jakarta.enterprise.context.Dependent
import no.roedt.Kilde
import no.roedt.fylke.FylkeService
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.postnummer.PostnummerService
import no.roedt.ringesentralen.brukere.RingesentralenGroupID

@Dependent
class VervingService(
    private val personService: PersonService,
    private val vervingRepository: VervingRepository,
    private val lokallagService: LokallagService,
    private val fylkeService: FylkeService,
    private val postnummerService: PostnummerService
) {
    fun postPersonSomSkalRinges(request: AutentisertVervingRequest): Pair<Boolean, Person> {
        val postnummer = postnummerService.findById(request.request.postnummer)
        vervingRepository.persist(
            Verving(
                telefonnummer = request.request.telefonnummer,
                fornavn = request.request.fornavn,
                etternavn = request.request.etternavn,
                postnummer = request.request.postnummer,
                verversNavn = request.request.verversNavn
            )
        )

        val vervaFraFoer = personService.finnFraTelefonnummer(request.request.telefonnummer)
        if (vervaFraFoer.isPresent) return Pair(false, vervaFraFoer.get())

        val person =
            Person(
                hypersysID = null,
                fornavn = request.request.fornavn,
                etternavn = request.request.etternavn,
                telefonnummer = request.request.telefonnummer,
                email = null,
                postnummer = postnummer,
                fylke = fylkeService.toFylke(postnummer),
                lokallag = lokallagService.fromPostnummer(postnummer),
                groupID = RingesentralenGroupID.ManglerSamtykke.nr,
                kilde = Kilde.Verva,
                sistOppdatert = null
            )
        personService.persist(person)
        return Pair(true, person)
    }

    fun mottaSvar(request: AutentisertMottaSvarRequest) {
        val erBruker =
            personService
                .finnFraTelefonnummer(request.request.telefonnummer)
                .map { it.groupID() }
                .filter { RingesentralenGroupID.isBrukerEllerVenter(it) }
        if (erBruker.isPresent) return

        val nextValue =
            if (request.request.svar) RingesentralenGroupID.PrioritertAaRinge else RingesentralenGroupID.Slett
        personService.oppdaterRolleFraTelefonnummer(nextValue.nr, request.request.telefonnummer)
    }
}
