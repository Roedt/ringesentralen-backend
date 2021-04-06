package no.roedt.ringesentralen.samtale

import com.nhaarman.mockitokotlin2.*
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.hypersys.RingerRepository
import no.roedt.ringesentralen.lokallag.LokallagRepository
import no.roedt.ringesentralen.person.Person
import no.roedt.ringesentralen.person.PersonRepository
import no.roedt.ringesentralen.person.UserId
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Valg21SpesifikkeResultat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RingServiceBeanTest {

    private val personRepository: PersonRepository = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val oppslagRepository: OppslagRepository = mock()
    private val persistentSamtaleRepository: PersistentSamtaleRepository = mock()
    private val oppfoelgingValg21Repository: OppfoelgingValg21Repository = mock()
    private val nesteMedlemAaRingeFinder: NesteMedlemAaRingeFinder = mock()
    private val lokallagRepository: LokallagRepository = mock()
    private val ringerRepository: RingerRepository = mock()

    private var ringService = RingServiceBean(personRepository = personRepository, databaseUpdater = databaseUpdater, oppslagRepository = oppslagRepository,
        samtaleRepository = persistentSamtaleRepository, oppfoelgingValg21Repository = oppfoelgingValg21Repository, nesteMedlemAaRingeFinder = nesteMedlemAaRingeFinder,
        lokallagRepository = lokallagRepository, ringerRepository = ringerRepository)

    @BeforeEach
    fun setup() {
        createRingbarPerson("Anders", "And", "12345678", 1, 1)
        createRingbarPerson("Andre", "Kvakk", "12345679", 2, 2)
    }

    @Test
    fun `samtale blir registrert`() {
        ringService = spy(ringService)
        doReturn(1).whenever(ringService).hypersysIDTilRingerId(any())
        doReturn(false).whenever(ringService).isBrukerEllerVenterPaaGodkjenning(any())

        val request = AutentisertResultatFraSamtaleRequest(
            UserId(1), ResultatFraSamtaleRequest(
                modus = Modus.medlemmer,
                ringtID = 2,
                resultat = Resultat.Svarte,
                kommentar = "Hei",
                modusspesifikkeResultat = Valg21SpesifikkeResultat(
                    vilHaKoronaprogram = false,
                    vilBliMerAktiv = false,
                    vilHaValgkampsbrev = false,
                    vilHaMedlemsLink = false,
                    vilHaNyhetsbrevLink = false
                ),
                vilIkkeBliRingt = false
        ),
            modus = Modus.velgere
        )

        val response = ringService.registrerResultatFraSamtale(request)
        assertNotNull(response)
    }

    @Test
    fun `resultattype som ikkje passar med modusen kastar feilmelding`() {
        val request = AutentisertResultatFraSamtaleRequest(
            userId = UserId(1),
            request = ResultatFraSamtaleRequest(
                modus = Modus.medlemmer,
                ringtID = 2,
                resultat = Resultat.Ringes_etter_valget,
                kommentar = "Hei",
                modusspesifikkeResultat = Valg21SpesifikkeResultat(
                    vilHaKoronaprogram = false,
                    vilBliMerAktiv = false,
                    vilHaValgkampsbrev = false,
                    vilHaMedlemsLink = false,
                    vilHaNyhetsbrevLink = false
                ),
                vilIkkeBliRingt = false
            ),
            modus = Modus.medlemmer
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