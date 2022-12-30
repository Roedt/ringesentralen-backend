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
import no.roedt.ringesentralen.samtale.OppfoelgingValg21Repository
import no.roedt.ringesentralen.samtale.PersistentSamtale
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
    private val oppfoelgingValg21Repository: OppfoelgingValg21Repository,
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

        val tidligereMedlemFrivillig =
            frivilligRepository.find("personId", personId).firstResultOptional<Frivillig>()
        tidligereMedlemFrivillig.ifPresent {
            aktivitetForFrivilligRepository.delete("frivillig_id=?1", it.id)
            frivilligKoronaRepository.delete("frivillig_id=?1", it.id)
            frivilligOpptattAvRepository.delete("frivillig_id=?1", it.id)
            kontaktRepository.delete("frivillig_id=?1", it.id)
            smsTilMottakerRepository.delete("mottaker_id=?1", it.id)
            frivilligRepository.deleteById(it.id)
        }
        kontaktRepository.delete("registrert_av=?1", personId)

        godkjenningRepository.delete("godkjentPerson=?1", personId)

        loginAttemptRepository.delete("hypersysID=?1", ikkeMedlemLenger.first.hypersysID)
        mfaRepository.delete("epost=?1", ikkeMedlemLenger.first.email)
        val ringt = samtaleRepository.find("ringt=?1", personId).list<PersistentSamtale>()
        ringt.forEach { slettSamtale(it) }

        val ikkeMedlemLengerRinger =
            ringerRepository.find("personId", personId).firstResultOptional<Ringer>()
        ikkeMedlemLengerRinger.ifPresent {
            godkjenningRepository.delete("godkjenner=?1", it.id)
            val ringer = samtaleRepository.find("ringer=?1", it.id).list<PersistentSamtale>()
            ringer.forEach { i -> slettSamtale(i) }
        }

        oppslagRepository.delete("ringt=?1", personId)
        oppslagRepository.delete("ringerHypersysId=?1", ikkeMedlemLenger.first.hypersysID)

        ringerRepository.delete("personId=?1", personId)
        personRepository.deleteById(personId)
    }

    private fun slettSamtale(it: PersistentSamtale) {
        oppfoelgingValg21Repository.delete("samtaleId", it.id)
        samtaleRepository.deleteById(it.id)
    }
}
