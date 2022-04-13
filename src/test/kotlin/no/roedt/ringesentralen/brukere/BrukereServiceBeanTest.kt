package no.roedt.ringesentralen.brukere

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.brukere.AutentisertTilgangsendringRequest
import no.roedt.brukere.FylkeRepository
import no.roedt.brukere.GodkjenningRepository
import no.roedt.brukere.TilgangsendringsRequest
import no.roedt.hypersys.HypersysService
import no.roedt.hypersys.ModelConverter
import no.roedt.lokallag.LokallagRepository
import no.roedt.person.GroupID
import no.roedt.person.Person
import no.roedt.person.PersonRepository
import no.roedt.person.UserId
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.person.RingerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.doReturn
import javax.ws.rs.ForbiddenException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class BrukereServiceBeanTest {

    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val fylkeRepository: FylkeRepository = mock()
    private val lokallagRepository: LokallagRepository = mock()
    private val epostSender: EpostSender = mock()
    private val hypersysService: HypersysService = mock()
    private val godkjenningRepository: GodkjenningRepository = mock()
    private val modelConverter: ModelConverter = mock()
    private val ringerRepository: RingerRepository = mock()

    private val brukereService = BrukereServiceBean(
        personRepository = personRepository,
        databaseUpdater = databaseUpdater,
        fylkeRepository = fylkeRepository,
        lokallagRepository = lokallagRepository,
        epostSender = epostSender,
        hypersysService = hypersysService,
        godkjenningRepository = godkjenningRepository,
        modelConverter = modelConverter,
        ringerRepository = ringerRepository
    )

    @Test
    fun `godkjenner kan godkjenne brukar`() {
        val userId = setupRinger()

        val ringt = Person()
        ringt.hypersysID = 3
        doReturn(ringt).whenever(personRepository).findById(2)

        doReturn(listOf(1L)).whenever(databaseUpdater).getResultList(any())

        val brukerendring = brukereService.aktiverRinger(
            AutentisertTilgangsendringRequest(
                userId = userId,
                tilgangsendringRequest = TilgangsendringsRequest(personMedEndraTilgang = 2),
                jwt = mock()
            )
        )
        assertEquals(2, brukerendring.personID)
        assertEquals(GroupID.GodkjentRinger, brukerendring.nyGroupId)
        assertTrue { brukerendring.epostSendt }
        verify(personRepository).update(any(), eq(GroupID.GodkjentRinger.nr), eq(2))
        verify(hypersysService).hentFraMedlemslista(3)
        verify(epostSender).sendEpostOmEndraStatus(person = eq(ringt), nyTilgang = eq(GroupID.GodkjentRinger))
    }

    @Test
    fun `godkjenner kan ikkje endre admin`() {
        val userId = setupRinger()

        val ringt = Person()
        ringt.hypersysID = 3
        ringt.setGroupID(GroupID.Admin)
        doReturn(ringt).whenever(personRepository).findById(2)

        doReturn(listOf(1L)).whenever(databaseUpdater).getResultList(any())

        assertThrows<ForbiddenException> {
            brukereService.aktiverRinger(
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
        ringerPerson.setGroupID(GroupID.LokalGodkjenner)
        val userId = UserId(userId = 1)
        val panacheQuery: PanacheQuery<Person> = mock()
        doReturn(ringerPerson).whenever(panacheQuery).firstResult<Person>()
        doReturn(panacheQuery).whenever(personRepository).find("hypersysID", 1)
        return userId
    }
}
