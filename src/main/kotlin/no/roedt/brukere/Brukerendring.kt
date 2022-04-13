package no.roedt.brukere

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.person.GroupID

@RegisterForReflection
data class Brukerendring(val personID: Int, val nyGroupId: GroupID, var epostSendt: Boolean)
