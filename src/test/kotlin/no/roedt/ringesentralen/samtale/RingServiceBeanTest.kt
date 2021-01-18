package no.roedt.ringesentralen.samtale

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
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
        createRingbarPerson("Anders", "And", "12345678", 1)
        createRingbarPerson("Andre", "Kvakk", "12345679", 2)
    }

    @Test
    fun `samtale blir registrert`() {
        doReturn(mockQuery).whenever(entityManager).createNativeQuery(any())

        val request = ResultatFraSamtaleRequest(
                modus = Modus.Korona,
                ringerID = 1,
                ringtID = 2,
                result = Resultat.Svarte,
                kommentar = "Hei",
                modusspesifikkeResultat = KoronaspesifikkeResultat(
                        vilHaKoronaprogram = false,
                        vilBliMerAktiv = false,
                        vilHaValgkampsbrev = false
                ),
                vilIkkeBliRingt = false
        )

        val response = ringService.registrerResultatFraSamtale(request)
        assertNotNull(response)
    }

    @Test
    fun `resultattype som ikkje passar med modusen kastar feilmelding`() {
        val request = ResultatFraSamtaleRequest(
                modus = Modus.Korona,
                ringerID = 1,
                ringtID = 2,
                result = Resultat.Ringes_etter_valget,
                kommentar = "Hei",
                modusspesifikkeResultat = KoronaspesifikkeResultat(
                        vilHaKoronaprogram = false,
                        vilBliMerAktiv = false,
                        vilHaValgkampsbrev = false
                ),
                vilIkkeBliRingt = false
        )

        assertThrows(AssertionError::class.java) {
            ringService.registrerResultatFraSamtale(request)
        }
    }

    private fun createRingbarPerson(fornavn: String, etternavn: String, telefonnummer: String, id: Long) {
        val person = RingbarPerson(hypersysID = null, givenName = fornavn, familyName = etternavn,
            phone = telefonnummer, lastCall = 0, email = "", nameEnlister = null,
            postnumber = "1234", countyID = 0, lokallag = 0)
        doReturn(person).whenever(personRepository).findById(id)
    }
}