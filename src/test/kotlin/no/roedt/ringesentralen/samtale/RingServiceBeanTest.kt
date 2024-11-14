package no.roedt.ringesentralen.samtale

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.quarkus.hibernate.orm.panache.PanacheQuery
import no.roedt.Kilde
import no.roedt.kommune.Kommune
import no.roedt.lokallag.LokallagService
import no.roedt.person.Person
import no.roedt.person.PersonService
import no.roedt.person.UserId
import no.roedt.postnummer.Postnummer
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
    private val personService: PersonService = mockk()
    private val samtaleService: SamtaleService = mockk()
    private val oppfoelgingValg21Service: OppfoelgingValg21Service = mockk()
    private val lokallagService: LokallagService = mockk()
    private val ringerService: RingerService = mockk()
    private val nestePersonAaRingeFinder: NestePersonAaRingeFinder = mockk()

    private var ringService =
        RingServiceBean(
            personService = personService,
            samtaleService = samtaleService,
            oppfoelgingValg21Service = oppfoelgingValg21Service,
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
        ringService = spyk(ringService)
        every { ringService.hypersysIDTilRingerId(any()) } returns 1
        every { ringService.isBrukerEllerVenterPaaGodkjenning(any()) } returns false

        val request =
            AutentisertResultatFraSamtaleRequest(
                UserId(1),
                ResultatFraSamtaleRequest(
                    modus = Modus.medlemmer,
                    ringtID = 2,
                    resultat = Resultat.Svarte,
                    kommentar = "Hei",
                    modusspesifikkeResultat =
                        Valg21SpesifikkeResultat(
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
        val request =
            AutentisertResultatFraSamtaleRequest(
                userId = UserId(1),
                request =
                    ResultatFraSamtaleRequest(
                        modus = Modus.medlemmer,
                        ringtID = 2,
                        resultat = Resultat.Ringes_etter_valget,
                        kommentar = "Hei",
                        modusspesifikkeResultat =
                            Valg21SpesifikkeResultat(
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
        val person =
            Person(
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
        every { personService.findById(id) } returns person
        val query: PanacheQuery<Person> = mockk()
        every { query.firstResult<Person>() } returns person
        every { personService.finnFraHypersysId(hypersysID) } returns query
    }
}
