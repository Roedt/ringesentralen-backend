package no.roedt.ringesentralen.samtale

import no.roedt.ringesentralen.UserId
import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class AutentisertResultatFraSamtaleRequest(val userId: UserId, val resultatFraSamtaleRequest: ResultatFraSamtaleRequest)