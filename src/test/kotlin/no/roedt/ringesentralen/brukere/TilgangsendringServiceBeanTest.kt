package no.roedt.ringesentralen.brukere

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.GodkjenningRepository
import no.roedt.brukere.TilgangsendringsRequest
import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.ModelConverter
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.RingesentralenGroupID
import no.roedt.person.UserId
import no.roedt.DatabaseUpdater
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doReturn
import javax.ws.rs.ForbiddenException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class TilgangsendringServiceBeanTest {

    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val epostSender: RingesentralenEpostformulerer = mock()
    private val hypersysService: HypersysService = mock()
    private val godkjenningRepository: GodkjenningRepository = mock()
    private val modelConverter: ModelConverter = mock()

    private val tilgangsendringService = TilgangsendringServiceBean(
        personRepository = personRepository,
        databaseUpdater = databaseUpdater,
        epostSender = epostSender,
        hypersysService = hypersysService,
        godkjenningRepository = godkjenningRepository,
        modelConverter = modelConverter
    )

    @Test
    fun `godkjenner kan godkjenne brukar`() {
        val userId = setupRinger()

        val ringt = Person()
        ringt.hypersysID = 3
        doReturn(ringt).whenever(personRepository).findById(2)

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
        verify(personRepository).update(any(), eq(RingesentralenGroupID.GodkjentRinger.nr), eq(2))
        verify(hypersysService).hentFraMedlemslista(3)
        verify(epostSender).sendEpostOmEndraStatus(person = eq(ringt), nyTilgang = eq(RingesentralenGroupID.GodkjentRinger))
    }

    @Test
    fun `godkjenner kan ikkje endre admin`() {
        val userId = setupRinger()

        val ringt = Person()
        ringt.hypersysID = 3
        ringt.setGroupID(RingesentralenGroupID.Admin)
        doReturn(ringt).whenever(personRepository).findById(2)

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
        val ringerPerson = Person()
        ringerPerson.setGroupID(RingesentralenGroupID.LokalGodkjenner)
        val userId = UserId(userId = 1)
        val panacheQuery: PanacheQuery<Person> = mock()
        doReturn(ringerPerson).whenever(panacheQuery).firstResult<Person>()
        doReturn(panacheQuery).whenever(personRepository).find("hypersysID", 1)
        return userId
    }
}
