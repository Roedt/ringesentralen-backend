package no.roedt.ringesentralen.brukere

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.DatabaseUpdater
import no.roedt.brukere.AutentisertGetBrukereRequest
import no.roedt.brukere.Brukerinformasjon
import no.roedt.brukere.FylkeRepository
import no.roedt.brukere.GenerellRolle
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerRepository
import no.roedt.tilNorskTid

interface BrukereService {
    fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon>
}

@ApplicationScoped
class BrukereServiceBean(
    val personService: PersonService,
    val databaseUpdater: DatabaseUpdater,
    val fylkeRepository: FylkeRepository,
    val lokallagService: LokallagService,
    val ringerRepository: RingerRepository
) : BrukereService {

    override fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon> =
        personService.listBrukere(request.groups.contains(GenerellRolle.admin), request.userId)
            .map(this::toBrukerinformasjon)

    private fun toBrukerinformasjon(r: Person) = Brukerinformasjon(
        id = r.id.toLong(),
        fornavn = r.fornavn,
        etternavn = r.etternavn,
        telefonnummer = r.telefonnummer,
        postnummer = r.postnummer.Postnummer,
        fylke = fylkeRepository.findById(r.fylke),
        epost = r.email ?: "",
        hypersysID = r.hypersysID ?: -1,
        lokallag = lokallagService.findById(r.lokallag),
        rolle = RingesentralenGroupID.from(r.groupID()).roller,
        registreringstidspunkt = ringerRepository.find("personId", r.id.toInt())
            .firstResult<Ringer>().oppretta.tilNorskTid()
    )
}
