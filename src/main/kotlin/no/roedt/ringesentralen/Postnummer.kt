package no.roedt.ringesentralen

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class Postnummer(val postnummer: String) {
    init {
        require(postnummer.length == 4)
    }

    fun getPostnummer()= Integer.parseInt(postnummer)

}
