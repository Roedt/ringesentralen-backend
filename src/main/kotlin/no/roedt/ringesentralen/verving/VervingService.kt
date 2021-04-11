package no.roedt.ringesentralen.verving

import no.roedt.ringesentralen.Kilde
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import javax.enterprise.context.Dependent

@Dependent
class VervingService(
    private val modelConverter: ModelConverter,
    private val personRepository: PersonRepository,
    private val vervingRepository: VervingRepository,
    private val lokallagRepository: LokallagRepository
) {

    fun postPersonSomSkalRinges(request: AutentisertVervingRequest) : Person? {
        val postnummer = request.request.postnummer
        vervingRepository.persist(Verving(
            telefonnummer = request.request.telefonnummer,
            fornavn = request.request.fornavn,
            etternavn = request.request.etternavn,
            postnummer = request.request.postnummer,
            ververID = request.request.verversNavn
        ))

        val vervaFraFoer = personRepository.find("telefonnummer=?1", request.request.telefonnummer).count() > 0L
        if (vervaFraFoer) return null

        return Person(
            hypersysID = null,
            fornavn = request.request.fornavn,
            etternavn = request.request.etternavn,
            telefonnummer = request.request.telefonnummer,
            email = null,
            postnummer = postnummer,
            fylke = modelConverter.toFylke(postnummer),
            lokallag = lokallagRepository.fromPostnummer(postnummer),
            groupID = GroupID.ManglerSamtykke.nr,
            kilde = Kilde.Verva
        )
    }

    fun mottaSvar(request: AutentisertMottaSvarRequest) {
        val nextValue = if (request.request.svar) GroupID.PrioritertAaRinge else GroupID.Slett
        personRepository.update("groupID=?1 where telefonnummer=?2", nextValue.nr, request.request.telefonnummer)
    }
}