package no.roedt.ringesentralen.brukere

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.quarkus.hibernate.orm.panache.PanacheQuery
import jakarta.ws.rs.ForbiddenException
import no.roedt.DatabaseUpdater
import no.roedt.Kilde
import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.GodkjenningService
import no.roedt.brukere.TilgangsendringsRequest
import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.konvertering.ModelConverter
import no.roedt.kommune.Kommune
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.UserId
import no.roedt.postnummer.Postnummer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doReturn
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TilgangsendringServiceBeanTest {

    private val personService: PersonService = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val epostSender: RingesentralenEpostformulerer = mock()
    private val hypersysService: HypersysService = mock()
    private val godkjenningService: GodkjenningService = mock()
    private val modelConverter: ModelConverter = mock()

    private val tilgangsendringService = TilgangsendringServiceBean(
        personService = personService,
        databaseUpdater = databaseUpdater,
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
        doReturn(ringt).whenever(personService).findById(2)

        doReturn(listOf(1L)).whenever(databaseUpdater).getResultList(any())

        val brukerendring = tilgangsendringService.aktiverRinger(
            AutentisertTilgangsendringRequest(
                userId = userId,
                tilgangsendringRequest = TilgangsendringsRequest(personMedEndraTilgang = 2),
                jwt = mock()
            )
        )
        assertEquals(2, brukerendring.personID)
        assertEquals(RingesentralenGroupID.GodkjentRinger, brukerendring.nyGroupId)
        assertTrue { brukerendring.epostSendt }
        verify(personService).oppdaterRolle(eq(RingesentralenGroupID.GodkjentRinger.nr), eq(2))
        verify(hypersysService).hentFraMedlemslista(3)
        verify(epostSender).sendEpostOmEndraStatus(
            person = eq(ringt),
            nyTilgang = eq(RingesentralenGroupID.GodkjentRinger)
        )
    }

    @Test
    fun `godkjenner kan ikkje endre admin`() {
        val userId = setupRinger()

        val ringt = lagPerson("Lars", "Holm", "+4792345678")
        ringt.hypersysID = 3
        ringt.setGroupID(RingesentralenGroupID.Admin)
        doReturn(ringt).whenever(personService).findById(2)

        doReturn(listOf(1L)).whenever(databaseUpdater).getResultList(any())

        assertThrows<ForbiddenException> {
            tilgangsendringService.aktiverRinger(
                AutentisertTilgangsendringRequest(
                    userId = userId,
                    tilgangsendringRequest = TilgangsendringsRequest(personMedEndraTilgang = 2),
                    jwt = mock()
                )
            )
        }
    }

    private fun setupRinger(): UserId {
        val ringerPerson = lagPerson("Peder", "Ås", "+4712345678")
        ringerPerson.setGroupID(RingesentralenGroupID.LokalGodkjenner)
        val userId = UserId(userId = 1)
        val panacheQuery: PanacheQuery<Person> = mock()
        doReturn(ringerPerson).whenever(panacheQuery).firstResult<Person>()
        doReturn(panacheQuery).whenever(personService).finnFraHypersysId(1)
        return userId
    }

    private fun lagPerson(fornavn: String, etternavn: String, telefonnummer: String) = Person(
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
