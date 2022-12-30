package no.roedt.brukere

import no.roedt.brukere.mfa.MFARepository
import no.roedt.frivilligsystem.Frivillig
import no.roedt.frivilligsystem.FrivilligKoronaRepository
import no.roedt.frivilligsystem.FrivilligOpptattAvRepository
import no.roedt.frivilligsystem.FrivilligRepository
import no.roedt.frivilligsystem.kontakt.KontaktRepository
import no.roedt.frivilligsystem.registrer.AktivitetForFrivilligRepository
import no.roedt.hypersys.externalModel.IsMember
import no.roedt.hypersys.login.LoginAttemptRepository
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerRepository
import no.roedt.ringesentralen.samtale.PersistentSamtaleRepository
import no.roedt.ringesentralen.samtale.start.OppslagRepository
import no.roedt.ringesentralen.sms.SMSTilMottakerRepository
import javax.enterprise.context.Dependent

@Dependent
class TidligereMedlemSletter(
    private val personRepository: PersonRepository,
    private val ringerRepository: RingerRepository,
    private val frivilligOpptattAvRepository: FrivilligOpptattAvRepository,
    private val frivilligKoronaRepository: FrivilligKoronaRepository,
    private val aktivitetForFrivilligRepository: AktivitetForFrivilligRepository,
    private val kontaktRepository: KontaktRepository,
    private val frivilligRepository: FrivilligRepository,
    private val godkjenningRepository: GodkjenningRepository,
    private val loginAttemptRepository: LoginAttemptRepository,
    private val mfaRepository: MFARepository,
    private val samtaleRepository: PersistentSamtaleRepository,
    private val oppslagRepository: OppslagRepository,
    private val smsTilMottakerRepository: SMSTilMottakerRepository
) {
    fun slett(ikkeMedlemLenger: Pair<Person, IsMember>) {
        val personId = ikkeMedlemLenger.first.id

        val tidligereMedlemPerson =
            personRepository.find("fornavn=?1 and etternavn=?2", "Tidligere", "Medlem").firstResult<Person>()
        val tidligereMedlemRinger =
            ringerRepository.find("personId", tidligereMedlemPerson.id).firstResult<Ringer>()

        flyttSamtalerOgKontaktTilGeneriskTidligereMedlem(
            tidligereMedlemPerson,
            personId,
            tidligereMedlemRinger,
            ikkeMedlemLenger
        )
        slettPersonenSomFrivilligSomRingerOgSomPersonISystemet(personId, ikkeMedlemLenger)
    }

    private fun flyttSamtalerOgKontaktTilGeneriskTidligereMedlem(
        tidligereMedlemPerson: Person,
        personId: Int,
        tidligereMedlemRinger: Ringer,
        ikkeMedlemLenger: Pair<Person, IsMember>
    ) {
        kontaktRepository.update("registrert_av=?1 where registrert_av=?2", tidligereMedlemPerson.id, personId)

        godkjenningRepository.update("godkjentPerson=?1 where godkjentPerson=?2", tidligereMedlemPerson.id, personId)

        samtaleRepository.update("ringt=?1 where ringt=?2", tidligereMedlemPerson.id, personId)

        val ikkeMedlemLengerRinger =
            ringerRepository.find("personId", personId).firstResultOptional<Ringer>()
        ikkeMedlemLengerRinger.ifPresent {
            godkjenningRepository.update("godkjenner=?1 where godkjenner=?2", tidligereMedlemRinger.id, it.id)
            samtaleRepository.update("ringer=?1 where ringer=?2", tidligereMedlemRinger.id, it.id)
        }

        oppslagRepository.update("ringt=?1 where ringt=?2", tidligereMedlemPerson.id, personId)
        oppslagRepository.update(
            "ringerHypersysId=?1 where ringerHypersysId=?2",
            tidligereMedlemPerson.hypersysID,
            ikkeMedlemLenger.first.hypersysID
        )
    }

    private fun slettPersonenSomFrivilligSomRingerOgSomPersonISystemet(
        personId: Int,
        ikkeMedlemLenger: Pair<Person, IsMember>
    ) {
        frivilligRepository.find("personId", personId).firstResultOptional<Frivillig>().ifPresent {
            aktivitetForFrivilligRepository.delete("frivillig_id=?1", it.id)
            frivilligKoronaRepository.delete("frivillig_id=?1", it.id)
            frivilligOpptattAvRepository.delete("frivillig_id=?1", it.id)
            kontaktRepository.delete("frivillig_id=?1", it.id)
            smsTilMottakerRepository.delete("mottaker_id=?1", it.id)
            frivilligRepository.deleteById(it.id)
        }
        loginAttemptRepository.delete("hypersysID=?1", ikkeMedlemLenger.first.hypersysID)
        mfaRepository.delete("epost=?1", ikkeMedlemLenger.first.email)
        ringerRepository.delete("personId=?1", personId)
        personRepository.deleteById(personId)
    }
}
