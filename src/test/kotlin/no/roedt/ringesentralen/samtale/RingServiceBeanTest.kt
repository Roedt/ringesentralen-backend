package no.roedt.ringesentralen.samtale

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.PersonRepository
import no.roedt.ringesentralen.hypersys.GyldigToken
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.AssertionError
import javax.persistence.EntityManager
import javax.persistence.Query

internal class RingServiceBeanTest {

    private val personRepository: PersonRepository = mock()
    private val entityManager: EntityManager = mock()

    private val ringService = RingServiceBean(personRepository = personRepository, entityManager = entityManager)

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
                token = GyldigToken("a", 1, "a", "a"),
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
                token = GyldigToken("a", 1, "a", "a"),
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
        val person = RingbarPerson(givenName = fornavn, familyName = etternavn, phone = telefonnummer, lastCall = 0, nameEnlister = null, postnumber = "1234", countyID = 0, lokallag = 0)
        doReturn(person).whenever(personRepository).findById(id)
    }
}