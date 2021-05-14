package no.roedt.ringesentralen.brukere

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.hypersys.HypersysService
import no.roedt.ringesentralen.hypersys.ModelConverter
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.GroupID
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.RingerRepository
import no.roedt.ringesentralen.person.UserId
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
        doReturn(ringt).whenever(personRepository).findById(2L)

        doReturn(listOf(1L)).whenever(databaseUpdater).getResultList(any())

        val brukerendring = brukereService.aktiverRinger(
            AutentisertTilgangsendringRequest(
                userId = userId,
                tilgangsendringRequest = TilgangsendringsRequest(personMedEndraTilgang = 2L),
                jwt = mock()
            )
        )
        assertEquals(2L, brukerendring.personID)
        assertEquals(GroupID.GodkjentRinger, brukerendring.nyGroupId)
        assertTrue { brukerendring.epostSendt }
        verify(personRepository).update(any(), eq(GroupID.GodkjentRinger.nr), eq(2L))
        verify(hypersysService).hentFraMedlemslista(3)
        verify(epostSender).sendEpost(person = eq(ringt), nyTilgang = eq(GroupID.GodkjentRinger))
    }

    @Test
    fun `godkjenner kan ikkje endre admin`() {
        val userId = setupRinger()

        val ringt = Person()
        ringt.hypersysID = 3
        ringt.setGroupID(GroupID.Admin)
        doReturn(ringt).whenever(personRepository).findById(2L)

        doReturn(listOf(1L)).whenever(databaseUpdater).getResultList(any())

        assertThrows<ForbiddenException> {
            brukereService.aktiverRinger(
                AutentisertTilgangsendringRequest(
                    userId = userId,
                    tilgangsendringRequest = TilgangsendringsRequest(personMedEndraTilgang = 2L),
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