package no.roedt.ringesentralen.samtale.start

import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.Roles
import no.roedt.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.samtale.OppfoelgingValg21Repository
import no.roedt.ringesentralen.samtale.Samtale
import java.sql.Timestamp
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.ForbiddenException
import javax.ws.rs.NotAuthorizedException

@ApplicationScoped
class NestePersonAaRingeFinder(
    val personRepository: PersonRepository,
    val databaseUpdater: DatabaseUpdater,
    val oppslagRepository: OppslagRepository,
    val oppfoelgingValg21Repository: OppfoelgingValg21Repository,
    val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder,
    val lokallagRepository: LokallagRepository,
    val nyligeOppslagCache: NyligeOppslagCache
) {

    fun hentNestePersonAaRinge(request: AutentisertNestePersonAaRingeRequest): NestePersonAaRingeResponse? {
        nyligeOppslagCache.assertAtIngenAndreSoekerIDetteLagetAkkuratNo(request.lokallag)
        return hentNestePersonAaRingeIDenneModusen(request)
            ?.let { personRepository.findById(it) }
            ?.let { toResponse(it) }
            ?.also { oppslagRepository.persist(Oppslag(ringt = it.person.id.toInt(), ringerHypersysId = request.userId())) }
            ?.also { nyligeOppslagCache.remove(request.lokallag) }
    }

    private fun hentNestePersonAaRingeIDenneModusen(request: AutentisertNestePersonAaRingeRequest): Int? =
        if (request.modus == Modus.velgere) hentNesteVelgerAaRinge(request)
        else hentNesteMedlemAaRinge(request)

    private fun hentNesteMedlemAaRinge(request: AutentisertNestePersonAaRingeRequest): Int? {
        if (!request.roller.contains(Roles.ringerMedlemmer))
            throw ForbiddenException("Kun dei godkjente for det kan ringe medlemmar")
        return nesteMedlemAaRingeFinder.hentIDForNesteMedlemAaRinge(personRepository.getPerson(request.userId), request.lokallag)
    }

    private fun hentNesteVelgerAaRinge(request: AutentisertNestePersonAaRingeRequest): Int? {
        val ringer = personRepository.getPerson(request.userId)
        if (ringer.lokallag != request.lokallag && !GroupID.referencesOneOf(
                ringer.groupID(),
                GroupID.LokalGodkjenner,
                GroupID.Admin
            )
        ) {
            throw NotAuthorizedException("Kun godkjennarar og admins kan ringe utanfor eiget lokallag", "")
        }
        return hentNestePerson(ringer, request.lokallag)
    }

    private fun hentNestePerson(ringer: Person, lokallag: Int) = databaseUpdater.getResultList(
        """SELECT v.id FROM v_personerSomKanRinges v 
                WHERE fylke = ${ringer.fylke} 
                AND hypersysID is null 
                ORDER BY ABS(lokallag-'$lokallag') ASC, 
                hypersysID DESC,
                brukergruppe = ${GroupID.PrioritertAaRinge.nr} DESC,
                sisteSamtale ASC,
                v.hypersysID DESC
        """
    )
        .map { it as Int }
        .firstOrNull()

    private fun toResponse(it: Person) =
        NestePersonAaRingeResponse(
            person = it,
            lokallagNavn = lokallagRepository.findById(it.lokallag).navn,
            tidlegareSamtalar = getTidlegareSamtalarMedDennePersonen(it.telefonnummer ?: "-1")
        )

    fun getTidlegareSamtalarMedDennePersonen(oppringtNummer: String): List<Samtale> =
        databaseUpdater.getResultList("SELECT resultat, ringerNavn, datetime, kommentar, ringtNavn, oppfoelgingId FROM `v_samtalerResultat` WHERE oppringtNummer = '$oppringtNummer'")
            .map { it as Array<*> }
            .map {
                Samtale(
                    resultat = it[0] as String,
                    ringer = it[1] as String,
                    tidspunkt = (it[2] as Timestamp).toString(),
                    kommentar = (it[3] ?: "") as String,
                    ringtNummer = oppringtNummer,
                    ringtNavn = it[4] as String,
                    oppfoelging = it[5]?.toString()?.let { i -> if (i != "null") oppfoelgingValg21Repository.findById(i.toInt()) else null }
                )
            }
            .toList()
}
