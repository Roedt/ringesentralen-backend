package no.roedt.brukere

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Brukerendring(val personID: Int, val nyGroupId: GroupID, var epostSendt: Boolean)
