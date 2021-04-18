
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponseOptions
import no.roedt.ringesentralen.Modus
import no.roedt.ringesentralen.hypersys.login.LoginRequest
import no.roedt.ringesentralen.samtale.resultat.Resultat
import no.roedt.ringesentralen.samtale.resultat.ResultatFraSamtaleRequest
import no.roedt.ringesentralen.samtale.resultat.Valg21SpesifikkeResultat
import no.roedt.ringesentralen.samtale.start.RingerTilbakeRequest
import no.roedt.ringesentralen.samtale.start.StartSamtaleRequest
import org.hamcrest.CoreMatchers.equalTo

class FullFlytTest {

//    @Test
    fun test() {
        val token = login()

        val ringtId: Long = 123452
        loginWithToken(token)
            .body(StartSamtaleRequest(skalRingesID = ringtId))
            .contentType(ContentType.JSON)
            .post("/samtale/startSamtale")
            .then()
            .statusCode(200)

        loginWithToken(token)
            .body(
                ResultatFraSamtaleRequest(
                modus = Modus.velgere,
                ringtID = ringtId,
                resultat = Resultat.Passet_ikke,
                kommentar = "Frå automatisk test",
                modusspesifikkeResultat = Valg21SpesifikkeResultat(
                    vilBliMerAktiv = true,
                    vilPolitikkLink = true,
                    vilBliRingtAugust = false,
                    vilHaMedlemsLink = true,
                    vilHaFellesskapLink = true
                ),
                vilIkkeBliRingt = false)
            )
            .contentType(ContentType.JSON)
            .post("/samtale/registrerResultatFraSamtale")
            .then()
            .statusCode(200)

        loginWithToken(token)
            .body(RingerTilbakeRequest(ringtNummer = "12345678"))
            .contentType(ContentType.JSON)
            .post("/samtale/noenRingerTilbake")
            .then()
            .statusCode(200)
            .body("fornavn", equalTo("Donald"))
    }

    private fun login(): String {
        val loginRequest = LoginRequest(brukarnamn = "dittBrukarnamn", key = "dinKey", passord = "dittPassord", systembruker = false)
        val response = given()
            .body(loginRequest)
            .contentType(ContentType.JSON)
            .`when`()
            .post("/token/login")
            .then()
            .statusCode(200)
        return response.responseAsString()
    }

    private fun loginWithToken(token: String) = given().`when`().header("Authorization", "Bearer $token")

    private fun Any.responseAsString() =(this as ValidatableResponseOptions<*, *>).log().all().extract().response().asString()
}