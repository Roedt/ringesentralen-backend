package no.roedt.ringesentralen.samtale

import UserId
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.PersonRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.persistence.EntityManager
import javax.persistence.Query

internal class RingServiceBeanTest {

    private val personRepository: PersonRepository = mock()
    private val entityManager: EntityManager = mock()
    private val databaseUpdater: DatabaseUpdater = mock()

    private val ringService = RingServiceBean(personRepository = personRepository, entityManager = entityManager, databaseUpdater = databaseUpdater )

    private val mockQuery: Query = mock()

    @BeforeEach
    fun setup() {
        createRingbarPerson("Anders", "And", "12345678", 1, 1)
        createRingbarPerson("Andre", "Kvakk", "12345679", 2, 2)
    }

    @Test
    fun `samtale blir registrert`() {
        doReturn(mockQuery).whenever(entityManager).createNativeQuery(any())

        val request = AutentisertResultatFraSamtaleRequest(
            UserId(1), ResultatFraSamtaleRequest(
                modus = Modus.Korona,
                ringtID = 2,
                result = Resultat.Svarte,
                kommentar = "Hei",
                modusspesifikkeResultat = KoronaspesifikkeResultat(
                        vilHaKoronaprogram = false,
                        vilBliMerAktiv = false,
                        vilHaValgkampsbrev = false
                ),
                vilIkkeBliRingt = false
        ))

        val response = ringService.registrerResultatFraSamtale(request)
        assertNotNull(response)
    }

    @Test
    fun `resultattype som ikkje passar med modusen kastar feilmelding`() {
        val request = AutentisertResultatFraSamtaleRequest(
            userId = UserId(1),
            ResultatFraSamtaleRequest(
                modus = Modus.Korona,
                ringtID = 2,
                result = Resultat.Ringes_etter_valget,
                kommentar = "Hei",
                modusspesifikkeResultat = KoronaspesifikkeResultat(
                        vilHaKoronaprogram = false,
                        vilBliMerAktiv = false,
                        vilHaValgkampsbrev = false
                ),
                vilIkkeBliRingt = false
        ))

        assertThrows(AssertionError::class.java) {
            ringService.registrerResultatFraSamtale(request)
        }
    }

    private fun createRingbarPerson(fornavn: String, etternavn: String, telefonnummer: String, id: Long, hypersysID: Int) {
        val person = RingbarPerson(hypersysID = hypersysID, fornavn = fornavn, etternavn = etternavn,
            phone = telefonnummer, lastCall = 0, email = "",
            postnummer = 1234, fylke = 0, lokallag = 0, groupID = 0)
        doReturn(person).whenever(personRepository).findById(id)
        val query : PanacheQuery<RingbarPerson> = mock()
        doReturn(person).whenever(query).firstResult<RingbarPerson>()
        doReturn(query).whenever(personRepository).find("hypersysID", hypersysID)
    }
}