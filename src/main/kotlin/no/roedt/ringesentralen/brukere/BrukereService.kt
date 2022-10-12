package no.roedt.ringesentralen.brukere

import no.roedt.DatabaseUpdater
import no.roedt.brukere.AutentisertGetBrukereRequest
import no.roedt.brukere.Brukerinformasjon
import no.roedt.brukere.FylkeRepository
import no.roedt.brukere.GenerelleRoller
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.person.Ringer
import no.roedt.ringesentralen.person.RingerRepository
import javax.enterprise.context.ApplicationScoped

interface BrukereService {
    fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon>
}

@ApplicationScoped
class BrukereServiceBean(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val fylkeRepository: FylkeRepository,
    val lokallagRepository: LokallagRepository,
    val ringerRepository: RingerRepository
) : BrukereService {

    override fun getBrukere(request: AutentisertGetBrukereRequest): List<Brukerinformasjon> {
        val brukersFylke = personRepository.find("hypersysID", request.userId.userId).firstResult<Person>().fylke
        val filtrerPaaFylke = if (request.groups.contains(GenerelleRoller.admin)) "" else "and fylke=$brukersFylke"
        return personRepository.list(
            "(groupID=?1 or groupID=?2 or groupID=?3 or groupID=?4 or groupID=?5 or groupID=?6) $filtrerPaaFylke",
            RingesentralenGroupID.UgodkjentRinger.nr,
            RingesentralenGroupID.AvslaattRinger.nr,
            RingesentralenGroupID.GodkjentRinger.nr,
            RingesentralenGroupID.GodkjentRingerMedlemmer.nr,
            RingesentralenGroupID.LokalGodkjenner.nr,
            RingesentralenGroupID.Admin.nr
        )
            .filter { !it.isSystembruker() }
            .map(this::toBrukerinformasjon)
    }

    private fun toBrukerinformasjon(r: Person) = Brukerinformasjon(
        id = r.id.toLong(),
        fornavn = r.fornavn,
        etternavn = r.etternavn,
        telefonnummer = r.telefonnummer,
        postnummer = r.postnummer,
        fylke = fylkeRepository.findById(r.fylke),
        epost = r.email ?: "",
        hypersysID = r.hypersysID ?: -1,
        lokallag = lokallagRepository.findById(r.lokallag),
        rolle = RingesentralenGroupID.from(r.groupID()).roller,
        registreringstidspunkt = ringerRepository.find("personId", r.id.toInt()).firstResult<Ringer>().oppretta
    )
}
