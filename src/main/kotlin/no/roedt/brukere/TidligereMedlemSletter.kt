package no.roedt.brukere

import jakarta.enterprise.context.Dependent
import no.roedt.brukere.mfa.MFAService
import no.roedt.frivilligsystem.Frivillig
import no.roedt.frivilligsystem.FrivilligKoronaRepository
import no.roedt.frivilligsystem.FrivilligOpptattAvRepository
import no.roedt.frivilligsystem.FrivilligRepository
import no.roedt.frivilligsystem.kontakt.KontaktRepository
import no.roedt.frivilligsystem.registrer.AktivitetForFrivilligRepository
import no.roedt.hypersys.login.LoginAttemptRepository
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerService
import no.roedt.ringesentralen.samtale.PersistentSamtaleRepository
import no.roedt.ringesentralen.samtale.start.OppslagRepository
import no.roedt.ringesentralen.sms.SMSTilMottakerRepository

@Dependent
class TidligereMedlemSletter(
    private val personService: PersonService,
    private val ringerService: RingerService,
    private val frivilligOpptattAvRepository: FrivilligOpptattAvRepository,
    private val frivilligKoronaRepository: FrivilligKoronaRepository,
    private val aktivitetForFrivilligRepository: AktivitetForFrivilligRepository,
    private val kontaktRepository: KontaktRepository,
    private val frivilligRepository: FrivilligRepository,
    private val godkjenningRepository: GodkjenningRepository,
    private val loginAttemptRepository: LoginAttemptRepository,
    private val mfaService: MFAService,
    private val samtaleRepository: PersistentSamtaleRepository,
    private val oppslagRepository: OppslagRepository,
    private val smsTilMottakerRepository: SMSTilMottakerRepository
) {
    fun slett(ikkeMedlemLenger: Person?) {
        if (ikkeMedlemLenger == null) {
            return
        }
        val personId = ikkeMedlemLenger.id

        val tidligereMedlemPerson = personService.finnFraNavn("Tidligere", "Medlem")
        val tidligereMedlemRinger =
            ringerService.finnFraPerson(tidligereMedlemPerson.id).firstResult<Ringer>()

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
        ikkeMedlemLenger: Person
    ) {
        kontaktRepository.update("registrert_av=?1 where registrert_av=?2", tidligereMedlemPerson.id, personId)

        godkjenningRepository.update("godkjentPerson=?1 where godkjentPerson=?2", tidligereMedlemPerson.id, personId)

        samtaleRepository.update("ringt=?1 where ringt=?2", tidligereMedlemPerson.id, personId)

        val ikkeMedlemLengerRinger = ringerService.finnFraPerson(personId).firstResultOptional<Ringer>()
        ikkeMedlemLengerRinger.ifPresent {
            godkjenningRepository.update("godkjenner=?1 where godkjenner=?2", tidligereMedlemRinger.id, it.id)
            samtaleRepository.update("ringer=?1 where ringer=?2", tidligereMedlemRinger.id, it.id)
        }

        oppslagRepository.update("ringt=?1 where ringt=?2", tidligereMedlemPerson.id, personId)
        oppslagRepository.update(
            "ringerHypersysId=?1 where ringerHypersysId=?2",
            tidligereMedlemPerson.hypersysID,
            ikkeMedlemLenger.hypersysID
        )
    }

    private fun slettPersonenSomFrivilligSomRingerOgSomPersonISystemet(
        personId: Int,
        ikkeMedlemLenger: Person
    ) {
        frivilligRepository.find("personId", personId).firstResultOptional<Frivillig>().ifPresent {
            aktivitetForFrivilligRepository.delete("frivillig_id=?1", it.id)
            frivilligKoronaRepository.delete("frivillig_id=?1", it.id)
            frivilligOpptattAvRepository.delete("frivillig_id=?1", it.id)
            kontaktRepository.delete("frivillig_id=?1", it.id)
            smsTilMottakerRepository.delete("mottaker_id=?1", it.id)
            frivilligRepository.deleteById(it.id)
        }
        loginAttemptRepository.delete("hypersysID=?1", ikkeMedlemLenger.hypersysID)
        mfaService.slett(ikkeMedlemLenger.email)
        ringerService.slett(personId)
        personService.deleteById(personId)
    }
}
