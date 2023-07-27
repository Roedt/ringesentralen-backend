package no.roedt.ringesentralen.samtale

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.whenever
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.DatabaseUpdater
import no.roedt.Kilde
import no.roedt.kommune.Kommune
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.Postnummer
import no.roedt.person.UserId
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.ringer.RingerService
import no.roedt.ringesentralen.samtale.resultat.AutentisertResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Valg21SpesifikkeResultat
import no.roedt.ringesentralen.samtale.start.NestePersonAaRingeFinder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class RingServiceBeanTest {

    private val personService: PersonService = mock()
    private val databaseUpdater: DatabaseUpdater = mock()
    private val persistentSamtaleRepository: PersistentSamtaleRepository = mock()
    private val oppfoelgingValg21Repository: OppfoelgingValg21Repository = mock()
    private val lokallagService: LokallagService = mock()
    private val ringerService: RingerService = mock()
    private val nestePersonAaRingeFinder: NestePersonAaRingeFinder = mock()

    private var ringService = RingServiceBean(
        personService = personService,
        databaseUpdater = databaseUpdater,
        samtaleRepository = persistentSamtaleRepository,
        oppfoelgingValg21Repository = oppfoelgingValg21Repository,
        lokallagService = lokallagService,
        ringerService = ringerService,
        nestePersonAaRingeFinder = nestePersonAaRingeFinder
    )

    @BeforeEach
    fun setup() {
        createRingbarPerson("Anders", "And", "12345678", 1, 1)
        createRingbarPerson("Andre", "Kvakk", "12345679", 2, 2)
    }

    // TODO Mads: følg opp denne
    fun `samtale blir registrert`() {
        ringService = spy(ringService)
        doReturn(1).whenever(ringService).hypersysIDTilRingerId(any())
        doReturn(false).whenever(ringService).isBrukerEllerVenterPaaGodkjenning(any())

        val request = AutentisertResultatFraSamtaleRequest(
            UserId(1),
            ResultatFraSamtaleRequest(
                modus = Modus.medlemmer,
                ringtID = 2,
                resultat = Resultat.Svarte,
                kommentar = "Hei",
                modusspesifikkeResultat = Valg21SpesifikkeResultat(
                    vilPolitikkLink = false,
                    vilBliMerAktiv = false,
                    vilBliRingtAugust = false,
                    vilHaMedlemsLink = false,
                    vilHaFellesskapLink = true
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
                    vilPolitikkLink = false,
                    vilBliMerAktiv = false,
                    vilBliRingtAugust = false,
                    vilHaMedlemsLink = false,
                    vilHaFellesskapLink = true
                ),
                vilIkkeBliRingt = false
            ),
            modus = Modus.medlemmer
        )

        assertThrows(AssertionError::class.java) {
            ringService.registrerResultatFraSamtale(request)
        }
    }

    private fun createRingbarPerson(
        fornavn: String,
        etternavn: String,
        telefonnummer: String,
        id: Int,
        hypersysID: Int
    ) {
        val person = Person(
            hypersysID = hypersysID,
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
        doReturn(person).whenever(personService).findById(id)
        val query: PanacheQuery<Person> = mock()
        doReturn(person).whenever(query).firstResult<Person>()
        doReturn(query).whenever(personService).finnFraHypersysId(hypersysID)
    }
}
