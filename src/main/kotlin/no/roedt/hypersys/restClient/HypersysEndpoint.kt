package no.roedt.hypersys.restClient

import io.quarkus.arc.profile.IfBuildProfile
import io.quarkus.security.Authenticated
import io.smallrye.jwt.build.Jwt
import jakarta.annotation.security.PermitAll
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.GyldigSystemToken
import no.roedt.hypersys.externalModel.IsMember
import no.roedt.hypersys.externalModel.Organisasjonsledd
import no.roedt.hypersys.externalModel.Profile
import no.roedt.hypersys.externalModel.User
import no.roedt.hypersys.externalModel.membership.Membership
import no.roedt.token.PrivateKeyFactory
import java.time.LocalDate

@IfBuildProfile(value = "dev")
@ApplicationScoped
@Path("hypersys")
class HypersysEndpoint(private val privateKeyFactory: PrivateKeyFactory) {

    val innloggaBrukerHypersysId = "15424"

    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("api/o/token/")
    fun tokenPerson() = GyldigPersonToken(
        access_token =Jwt
                .audience("ringer")
            .issuer("https://ringesentralen.no")
            .subject("FakeRingesentralen")
            .upn("FakeRingesentralen")
            .issuedAt(System.currentTimeMillis())
            .expiresAt(System.currentTimeMillis() + (3600*24))
            .groups(setOf())
            .claim("hypersys.user_id", innloggaBrukerHypersysId) // Donald
            .sign(privateKeyFactory.readPrivateKey()),
//        access_token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.NHVaYe26MbtOYhSKkoKYdFVomg4i8ZJd8_-RU8VNbftc4TSMb4bXP3l3YlNWACwyXPGffz5aXHc6lty1Y2t4SWRqGteragsVdZufDn5BlnJl9pdR_kdVFUsra2rWKEofkZeIC4yWytE58sMIihvo9H1ScmmVwBcQP6XETqYd0aSHp1gOa9RdUPDvoXQ5oqygTqVtxaDr6wUFKrKItgBMzWIdNZ6y7O9E0DhEPTbE9rfBo6KTFsHAZnMg4k68CDp2woYIaXbmYTWcvbzIuHO7_37GT79XdIwkm95QJ7hYC9RiwrV7mesbY4PAahERJawntho0my942XheVLmGwLMBkQ",
        expires_in = 3600 * 24,
        token_type = "TokenType1",
        scope = "Scope1",
        refresh_token = "RefreshToken1",
        user_id = innloggaBrukerHypersysId
    )

    @PermitAll
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("api/o/token/system")
    fun tokenSystem(@FormParam("grant_type") grantType: String = "client_credentials"): GyldigSystemToken = GyldigSystemToken(
        access_token = "Systemtoken1",
        expires_in = 3600 * 24,
        token_type = "TokenType2",
        scope = "Scope2",
    )

    @GET
    @Authenticated
    @Path("/membership/api/membership/{hypersysLokallagId}/{aar}/")
    fun hentMedlemmerILag(
        @PathParam("hypersysLokallagId") hypersysLokallagId: Int,
        @PathParam("aar") aar: Int = LocalDate.now().year,
    ): List<Membership> = listOf()

    @Authenticated
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/org/api/")
    fun hentAlleLokallag(): List<Organisasjonsledd> = listOf()

    @Authenticated
    @GET
    @Path("/membership/api/is_member/{hypersysId}/")
    fun hentPerson(@PathParam("hypersysId") hypersysId: Int): IsMember = IsMember(
        user_id = hypersysId,
        name = "Peder Ås",
        paid = true,
        is_member = true,
    )

    @GET
    @Path("actor/api/profile/")
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    fun hentProfil(): Profile = Profile(
        user = User(
            id = Integer.parseInt(innloggaBrukerHypersysId),
            name = "Peder Ås",
            email = "peder@aas.com",
            phone = "",
            phone2 = "",
            roles = listOf(),
            memberships = listOf(),
            addresses = listOf()
        )
    )
}