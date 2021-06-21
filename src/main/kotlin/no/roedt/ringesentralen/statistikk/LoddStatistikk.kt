package no.roedt.ringesentralen.statistikk

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class LoddStatistikk(
    val fornavn: String,
    val etternavn: String,
    val lokallag: String,
    val antallSamtaler: Int
)
