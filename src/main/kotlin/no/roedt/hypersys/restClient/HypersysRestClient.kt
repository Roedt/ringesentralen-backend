package no.roedt.hypersys.restClient

import io.quarkus.rest.client.reactive.ClientQueryParam
import io.quarkus.rest.client.reactive.NotBody
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MediaType
import no.roedt.hypersys.GyldigPersonToken
import no.roedt.hypersys.GyldigSystemToken
import no.roedt.hypersys.externalModel.IsMember
import no.roedt.hypersys.externalModel.Organisasjonsledd
import no.roedt.hypersys.externalModel.Profile
import no.roedt.hypersys.externalModel.membership.Membership
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import java.time.LocalDate

@Path("")
@ApplicationScoped
@RegisterRestClient
interface HypersysRestClient {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("api/o/token/")
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {base64Credentials}"])
    @ClientQueryParam(name = "grant_type", value = ["password"])
    @ClientQueryParam(name = "username", value = ["{brukernavn}"])
    @ClientQueryParam(name = "password", value = ["{passord}"])
    fun tokenPerson(
        @NotBody base64Credentials: String,
        @NotBody brukernavn: String,
        @NotBody passord: String
    ): GyldigPersonToken

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("api/o/token/")
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {base64Credentials}"])
    @ClientQueryParam(name = "grant_type", value = ["client_credentials"])
    fun tokenSystem(
        @NotBody base64Credentials: String
    ): GyldigSystemToken

    @GET
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    @Path("/membership/api/membership/{hypersysLokallagId}/{aar}/")
    fun hentMedlemmerILag(
        @PathParam("hypersysLokallagId") hypersysLokallagId: Int,
        @PathParam("aar") aar: Int = LocalDate.now().year,
        @NotBody token: String
    ) : List<Membership>

    @GET
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    @Path("/org/api")
    fun hentAlleLokallag(
        @NotBody token: String
    ) : List<Organisasjonsledd>

    @GET
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    @Path("/membership/api/is_member/{hypersysId}/")
    fun hentPerson(
        @PathParam("hypersysId") hypersysId: Int,
        @NotBody token: String
    ) : IsMember

    @GET
    @ClientHeaderParam(name = "Authorization", value = ["Bearer {token}"])
    @Path("actor/api/profile/")
    fun hentProfil(
        @NotBody token: String
    ) : Profile
}