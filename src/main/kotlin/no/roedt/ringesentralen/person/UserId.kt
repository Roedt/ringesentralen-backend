package no.roedt.ringesentralen.person

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class UserId(val userId: Int)
