package no.roedt.ringesentralen.hypersys

import no.roedt.ringesentralen.Brukarinformasjon
import no.roedt.ringesentralen.DatabaseUpdater
import no.roedt.ringesentralen.hypersys.externalModel.Profile
import no.roedt.ringesentralen.token.GCPSecretManager
import javax.enterprise.context.Dependent

@Dependent
class HypersysLoginBean(
    private val hypersysProxy: HypersysProxy,
    private val databaseUpdater: DatabaseUpdater,
    private val modelConverter: ModelConverter,
    private val gcpSecretManager: GCPSecretManager
) {
    fun login(loginRequest: LoginRequest): Token {
        val brukarId = gcpSecretManager.getHypersysBrukerId()
        val brukarSecret = gcpSecretManager.getHypersysBrukerSecret()
        val response = hypersysProxy.post(brukarId, brukarSecret, "grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        if (response.statusCode() != 200) {
            return hypersysProxy.readResponse(response, UgyldigToken::class.java)
        }

        val gyldigToken = hypersysProxy.readResponse(response, GyldigPersonToken::class.java)
        oppdaterRingerFraaHypersys(gyldigToken)
        return gyldigToken
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigPersonToken) {
        val profile: Profile = hypersysProxy.get("actor/api/profile/", token, Profile::class.java)
        val brukarinformasjon: Brukarinformasjon = modelConverter.convert(profile)

        databaseUpdater.update(brukarinformasjon.toSQL())
    }

    private fun Brukarinformasjon.toSQL(): String = "CALL sp_registrerNyBruker(" +
            "'${hypersysID}', " +
            "'${fornamn}', " +
            "'${etternamn}', " +
            "'${telefonnummer.nummer}', " +
            "'${epost}', " +
            "${postnummer.postnummer}, " +
            "${fylke.nr}" +
            ")"
}