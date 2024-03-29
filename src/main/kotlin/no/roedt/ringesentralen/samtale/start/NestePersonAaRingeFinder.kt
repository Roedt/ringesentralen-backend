package no.roedt.ringesentralen.samtale.start

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.ForbiddenException
import jakarta.ws.rs.NotAuthorizedException
import no.roedt.brukere.GroupID
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonDTO
import no.roedt.person.PersonService
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.RingespesifikkRolle
import no.roedt.ringesentralen.brukere.RingesentralenGroupID
import no.roedt.ringesentralen.samtale.OppfoelgingValg21Service
import no.roedt.ringesentralen.samtale.Samtale
import no.roedt.ringesentralen.samtale.oppslag.Oppslag
import no.roedt.ringesentralen.samtale.oppslag.OppslagService
import no.roedt.skrivUt
import java.sql.Timestamp

@ApplicationScoped
class NestePersonAaRingeFinder(
    val personService: PersonService,
    val oppslagService: OppslagService,
    val oppfoelgingValg21Service: OppfoelgingValg21Service,
    val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder,
    val lokallagService: LokallagService,
    val nyligeOppslagCache: NyligeOppslagCache,
    val nesteAaRingeRepository: NesteAaRingeRepository
) {
    fun hentNestePersonAaRinge(request: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse? {
        nyligeOppslagCache.assertAtIngenAndreSoekerIDetteLagetAkkuratNo(request.lokallag)
        return hentNestePersonAaRingeIDenneModusen(request)
            ?.let { personService.findById(it) }
            ?.let { toResponse(it) }
            ?.also { oppslagService.persist(Oppslag(ringt = it.person.id, ringerHypersysId = request.userId())) }
            ?.also { nyligeOppslagCache.remove(request.lokallag) }
    }

    private fun hentNestePersonAaRingeIDenneModusen(request: AutentisertNestePersonAaRingeRequest): Int? =
        if (request.modus == Modus.velgere) {
            hentNesteVelgerAaRinge(request)
        } else {
            hentNesteMedlemAaRinge(request)
        }

    private fun hentNesteMedlemAaRinge(request: AutentisertNestePersonAaRingeRequest): Int? {
        if (!request.roller.contains(RingespesifikkRolle.RINGER_MEDLEMMER)) {
            throw ForbiddenException("Kun dei godkjente for det kan ringe medlemmar")
        }
        return nesteMedlemAaRingeFinder.hentIDForNesteMedlemAaRinge(
            personService.getPerson(request.userId),
            request.lokallag
        )
    }

    private fun hentNesteVelgerAaRinge(request: AutentisertNestePersonAaRingeRequest): Int? {
        val ringer = personService.getPerson(request.userId)
        if (ringer.lokallag != request.lokallag &&
            !GroupID.referencesOneOf(
                ringer.groupID(),
                RingesentralenGroupID.LokalGodkjenner,
                RingesentralenGroupID.Admin
            )
        ) {
            throw NotAuthorizedException("Kun godkjennarar og admins kan ringe utanfor eiget lokallag", "")
        }
        return hentNestePerson(ringer, request.lokallag)
    }

    private fun hentNestePerson(
        ringer: Person,
        lokallag: Int
    ) = nesteAaRingeRepository.hentNesteIkkemedlem(ringer.fylke, lokallag)
        .map { it as Int }
        .firstOrNull()

    private fun toResponse(it: Person) =
        NestePersonAaRingeResponse(
            person = PersonDTO.fra(it),
            lokallagNavn = lokallagService.findById(it.lokallag).navn,
            tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1")
        )

    fun getTidlegareSamtalarMedDennePersonen(oppringtNummer: String): List<Samtale> =
        nesteAaRingeRepository.getTidlegareSamtalarMedDennePersonen(oppringtNummer)
            .map { it as Array<*> }
            .map {
                Samtale(
                    resultat = it[0] as String,
                    ringer = it[1] as String,
                    tidspunkt = (it[2] as Timestamp).skrivUt(),
                    kommentar = (it[3] ?: "") as String,
                    ringtNummer = oppringtNummer,
                    ringtNavn = it[4] as String,
                    oppfoelging =
                        it[5]?.toString()
                            ?.let { i -> if (i != "null") oppfoelgingValg21Service.findById(i.toInt()) else null }
                )
            }
            .toList()
}
