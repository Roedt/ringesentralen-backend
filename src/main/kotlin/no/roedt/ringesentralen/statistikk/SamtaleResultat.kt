package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class SamtaleResultat(val displaytext: String, val antal: Int)
