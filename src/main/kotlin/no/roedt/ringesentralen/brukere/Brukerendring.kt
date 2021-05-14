package no.roedt.ringesentralen.brukere

import io.quarkus.runtime.annotations.RegisterForReflection
import no.roedt.ringesentralen.person.GroupID

@RegisterForReflection
data class Brukerendring(val personID: Long, val nyGroupId: GroupID, var epostSendt: Boolean)
