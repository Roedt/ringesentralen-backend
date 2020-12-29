package no.roedt.ringesentralen

data class Postnummer(val postnummer: String) {
    init {
        require(postnummer.length == 4)
    }

    fun getPostnummer()= Integer.parseInt(postnummer)

}
