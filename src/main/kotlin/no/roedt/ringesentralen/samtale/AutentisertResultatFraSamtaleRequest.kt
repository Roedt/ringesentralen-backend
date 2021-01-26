package no.roedt.ringesentralen.samtale

import UserId
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AutentisertResultatFraSamtaleRequest(val userId: UserId, val resultatFraSamtaleRequest: ResultatFraSamtaleRequest)