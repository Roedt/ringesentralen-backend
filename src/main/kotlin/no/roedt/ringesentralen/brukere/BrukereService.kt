package no.roedt.ringesentralen.brukere

import jakarta.enterprise.context.ApplicationScoped
import no.roedt.brukere.AutentisertGetBrukereRequest
import no.roedt.brukere.Brukerinformasjon
import no.roedt.brukere.GenerellRolle
import no.roedt.fylke.FylkeService
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerService
import no.roedt.tilNorskTid

interface BrukereService {
    fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon>
}

@ApplicationScoped
class BrukereServiceBean(
    val personService: PersonService,
    val fylkeService: FylkeService,
    val lokallagService: LokallagService,
    val ringerService: RingerService
) : BrukereService {
    override fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon> =
        personService.listBrukere(request.groups.contains(GenerellRolle.ADMIN), request.userId)
            .map(this::toBrukerinformasjon)

    private fun toBrukerinformasjon(r: Person) =
        Brukerinformasjon(
            id = r.id.toLong(),
            fornavn = r.fornavn,
            etternavn = r.etternavn,
            telefonnummer = r.telefonnummer,
            postnummer = r.postnummer.Postnummer,
            fylke = fylkeService.findById(r.fylke),
            epost = r.email ?: "",
            hypersysID = r.hypersysID ?: -1,
            lokallag = lokallagService.findById(r.lokallag),
            rolle = RingesentralenGroupID.from(r.groupID()).roller,
            registreringstidspunkt = ringerService.finnFraPerson(r.id.toInt()).firstResult<Ringer>().oppretta.tilNorskTid()
        )
}
