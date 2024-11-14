package no.roedt.ringesentralen.brukere

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import io.quarkus.hibernate.orm.panache.PanacheQuery
import jakarta.ws.rs.ForbiddenException
import no.roedt.Kilde
import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.GodkjenningService
import no.roedt.brukere.TilgangsendringsRequest
import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.externalModel.membership.Membership
import no.roedt.hypersys.konvertering.ModelConverter
import no.roedt.kommune.Kommune
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.UserId
import no.roedt.postnummer.Postnummer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TilgangsendringServiceBeanTest {
    private val personService: PersonService = mockk()
    private val epostSender: RingesentralenEpostformulerer = mockk()
    private val hypersysService: HypersysService = mockk()
    private val godkjenningService: GodkjenningService = mockk()
    private val modelConverter: ModelConverter = mockk()

    private fun lagTilgangsendringservice() = TilgangsendringServiceBean(
        personService = personService,
        epostSender = epostSender,
        hypersysService = hypersysService,
        godkjenningService = godkjenningService,
        modelConverter = modelConverter
    )

    @Test
    fun `godkjenner kan godkjenne brukar`() {
        val userId = setupRinger()

        val ringt = lagPerson("Lars", "Holm", "+4792345678")
        ringt.hypersysID = 3
        every { personService.hypersysIDTilRingerId(any()) } returns 2
        every { personService.findById(2) } returns ringt
        every { godkjenningService.persist(any()) } just runs
        every { personService.oppdaterRolle(any(), any()) } returns 1
        every { hypersysService.hentFraMedlemslista(3) } returns mockk<Membership>().also {
            every { it.first_name } returns "Peder"
            every { it.last_name } returns "Aas"
        }
        every { personService.oppdaterNavnFraHypersys(any(), any(), any(), any()) } returns 1
        every { modelConverter.finnPostnummer(any()) } returns Postnummer(Postnummer = "0001", Poststed = "Oslo", KommuneKode = mockk())
        every { epostSender.sendEpostOmEndraStatus(any(), any()) } just runs
        val tilgangsendringService = lagTilgangsendringservice()

        val brukerendring =
            tilgangsendringService.aktiverRinger(
                AutentisertTilgangsendringRequest(
                    userId = userId,
                    tilgangsendringRequest = TilgangsendringsRequest(personMedEndraTilgang = 2),
                    jwt = mockk()
                )
            )
        assertEquals(2, brukerendring.personID)
        assertEquals(RingesentralenGroupID.GodkjentRinger, brukerendring.nyGroupId)
        assertTrue { brukerendring.epostSendt }

        verify { personService.oppdaterRolle(RingesentralenGroupID.GodkjentRinger.nr, 2) }
        verify { hypersysService.hentFraMedlemslista(3) }
        verify { epostSender.sendEpostOmEndraStatus(person = ringt, nyTilgang = RingesentralenGroupID.GodkjentRinger) }
    }

    @Test
    fun `godkjenner kan ikkje endre admin`() {
        val userId = setupRinger()

        val ringt = lagPerson("Lars", "Holm", "+4792345678")
        ringt.hypersysID = 3
        ringt.setGroupID(RingesentralenGroupID.Admin)
        every { personService.findById(2) } returns ringt
        val tilgangsendringService = lagTilgangsendringservice()


        assertThrows<ForbiddenException> {
            tilgangsendringService.aktiverRinger(
                AutentisertTilgangsendringRequest(
                    userId = userId,
                    tilgangsendringRequest = TilgangsendringsRequest(personMedEndraTilgang = 2),
                    jwt = mockk()
                )
            )
        }
    }

    private fun setupRinger(): UserId {
        val ringerPerson = lagPerson("Peder", "Ås", "+4712345678")
        ringerPerson.setGroupID(RingesentralenGroupID.LokalGodkjenner)
        val userId = UserId(userId = 1)
        val panacheQuery: PanacheQuery<Person> = mockk()
        every { panacheQuery.firstResult<Person>() } returns ringerPerson
        every { personService.finnFraHypersysId(1) } returns panacheQuery
        return userId
    }

    private fun lagPerson(
        fornavn: String,
        etternavn: String,
        telefonnummer: String
    ) = Person(
        hypersysID = 123,
        fornavn = fornavn,
        etternavn = etternavn,
        telefonnummer = telefonnummer,
        email = "",
        postnummer = Postnummer("1234", "Lillevik", Kommune("", "", 0, 0)),
        fylke = 0,
        lokallag = 0,
        groupID = 0,
        kilde = Kilde.Hypersys,
        sistOppdatert = null
    )
}
