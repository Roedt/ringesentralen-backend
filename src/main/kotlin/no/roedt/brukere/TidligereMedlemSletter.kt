package no.roedt.brukere

import jakarta.enterprise.context.Dependent
import no.roedt.brukere.mfa.MFAService
import no.roedt.frivilligsystem.FrivilligKoronaService
import no.roedt.frivilligsystem.FrivilligOpptattAvService
import no.roedt.frivilligsystem.FrivilligService
import no.roedt.frivilligsystem.kontakt.KontaktService
import no.roedt.frivilligsystem.registrer.AktivitetForFrivilligService
import no.roedt.hypersys.login.LoginService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.ringesentralen.ringer.Ringer
import no.roedt.ringesentralen.ringer.RingerService
import no.roedt.ringesentralen.samtale.SamtaleService
import no.roedt.ringesentralen.samtale.oppslag.OppslagService
import no.roedt.ringesentralen.sms.SMSTilMottakerService

@Dependent
class TidligereMedlemSletter(
    private val personService: PersonService,
    private val ringerService: RingerService,
    private val frivilligOpptattAvService: FrivilligOpptattAvService,
    private val frivilligKoronaService: FrivilligKoronaService,
    private val aktivitetForFrivilligService: AktivitetForFrivilligService,
    private val kontaktService: KontaktService,
    private val frivilligService: FrivilligService,
    private val godkjenningService: GodkjenningService,
    private val loginService: LoginService,
    private val mfaService: MFAService,
    private val samtaleService: SamtaleService,
    private val oppslagService: OppslagService,
    private val smsTilMottakerService: SMSTilMottakerService
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
        kontaktService.flyttKontaktTilGeneriskTidligereMedlem(tidligereMedlemPerson.id, personId)

        samtaleService.flyttSamtalerMedDenneSomRingt(tidligereMedlemPerson.id, personId)

        val ikkeMedlemLengerRinger = ringerService.finnFraPerson(personId).firstResultOptional<Ringer>()

        godkjenningService.flyttGodkjenningerTilGeneriskTidligereMedlem(
            tidligereMedlemPerson.id,
            tidligereMedlemRinger,
            personId,
            ikkeMedlemLengerRinger
        )

        ikkeMedlemLengerRinger.ifPresent {
            samtaleService.flyttSamtalerMedDenneSomRinger(tidligereMedlemRinger.id, it.id)
        }

        oppslagService.flyttTilGeneriskTidligereMedlem(
            tidligereMedlemPerson,
            personId,
            ikkeMedlemLenger
        )
    }

    private fun slettPersonenSomFrivilligSomRingerOgSomPersonISystemet(
        personId: Int,
        ikkeMedlemLenger: Person
    ) {
        frivilligService.finnFraPersonId(personId).ifPresent {
            aktivitetForFrivilligService.slett(it.id)
            frivilligKoronaService.slett(it.id)
            frivilligOpptattAvService.slett(it.id)
            kontaktService.slett(it.id)
            smsTilMottakerService.slett(it.id)
            frivilligService.slett(it.id)
        }
        loginService.slett(ikkeMedlemLenger.hypersysID)
        mfaService.slett(ikkeMedlemLenger.email)
        ringerService.slett(personId)
        personService.deleteById(personId)
    }
}
