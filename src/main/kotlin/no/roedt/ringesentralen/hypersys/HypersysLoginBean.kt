package no.roedt.ringesentralen.hypersys

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
        val brukerId = gcpSecretManager.getHypersysBrukerId()
        val brukerSecret = gcpSecretManager.getHypersysBrukerSecret()
        val response = hypersysProxy.post(brukerId, brukerSecret, "grant_type=password&username=${loginRequest.brukarnamn}&password=${loginRequest.passord}")
        if (response.statusCode() != 200) {
            return hypersysProxy.readResponse(response, UgyldigToken::class.java)
        }

        val gyldigToken = hypersysProxy.readResponse(response, GyldigPersonToken::class.java)
        oppdaterRingerFraaHypersys(gyldigToken)
        return gyldigToken
    }

    private fun oppdaterRingerFraaHypersys(token: GyldigPersonToken) {
        val profile: Profile = hypersysProxy.get("actor/api/profile/", token, Profile::class.java)
        val brukerinformasjon: String = modelConverter.convertToSQL(profile)
        databaseUpdater.update(brukerinformasjon, "CALL sp_recordLoginAttempt(${profile.user.id})")
    }
}