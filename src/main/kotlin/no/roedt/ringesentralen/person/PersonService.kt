package no.roedt.ringesentralen.person

import no.roedt.ringesentralen.hypersys.ModelConverter
import javax.enterprise.context.Dependent

@Dependent
class PersonService(
    private val modelConverter: ModelConverter,
    private val personRepository: PersonRepository
) {

    fun postPersonSomSkalRinges(request: AutentisertPersonSomSkalRingesRequest) : Any {
        val postnummer = request.request.postnummer
        return Person(
            hypersysID = null,
            fornavn = request.request.fornavn,
            etternavn = request.request.etternavn,
            telefonnummer = request.request.telefonnummer,
            email = null,
            postnummer = postnummer,
            fylke = modelConverter.toFylke(postnummer),
            lokallag = modelConverter.toLokallag(postnummer),
            groupID = GroupID.ManglerSamtykke.nr
        )
    }

    fun mottaSvar(request: AutentisertMottaSvarRequest) {
        val nextValue = if (request.request.svar) GroupID.KlarTilAaRinges else GroupID.Slett
        personRepository.update("groupID=?1 where telefonnummer=?2", nextValue.nr, request.request.telefonnummer)
    }
}