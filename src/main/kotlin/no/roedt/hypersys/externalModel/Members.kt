package no.roedt.hypersys.externalModel

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Members(val members: List<Member>)
