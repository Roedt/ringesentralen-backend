package no.roedt.ringesentralen.samtale

import com.nhaarman.mockitokotlin2.*
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.KoronaspesifikkeResultat
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RingServiceBeanTest {

    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()

    private var ringService = RingServiceBean(personRepository = personRepository, databaseUpdater = databaseUpdater )

    @BeforeEach
    fun setup() {
        createRingbarPerson("Anders", "And", "12345678", 1, 1)
        createRingbarPerson("Andre", "Kvakk", "12345679", 2, 2)
    }

    @Test
    fun `samtale blir registrert`() {
        ringService = spy(ringService)
        doReturn(1).whenever(ringService).hypersysIDTilRingerId(any())

        val request = AutentisertResultatFraSamtaleRequest(
            UserId(1), ResultatFraSamtaleRequest(
                modus = Modus.Korona,
                ringtID = 2,
                resultat = Resultat.Svarte,
                kommentar = "Hei",
                modusspesifikkeResultat = KoronaspesifikkeResultat(
                        vilHaKoronaprogram = false,
                        vilBliMerAktiv = false,
                        vilHaValgkampsbrev = false
                ),
                vilIkkeBliRingt = false
        )
        )

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
                resultat = Resultat.Ringes_etter_valget,
                kommentar = "Hei",
                modusspesifikkeResultat = KoronaspesifikkeResultat(
                        vilHaKoronaprogram = false,
                        vilBliMerAktiv = false,
                        vilHaValgkampsbrev = false
                ),
                vilIkkeBliRingt = false
        )
        )

        assertThrows(AssertionError::class.java) {
            ringService.registrerResultatFraSamtale(request)
        }
    }

    private fun createRingbarPerson(fornavn: String, etternavn: String, telefonnummer: String, id: Long, hypersysID: Int) {
        val person = Person(hypersysID = hypersysID, fornavn = fornavn, etternavn = etternavn,
            telefonnummer = telefonnummer, email = "",
            postnummer = 1234, fylke = 0, lokallag = 0, groupID = 0)
        doReturn(person).whenever(personRepository).findById(id)
        val query : PanacheQuery<Person> = mock()
        doReturn(person).whenever(query).firstResult<Person>()
        doReturn(query).whenever(personRepository).find("hypersysID", hypersysID)
    }
}