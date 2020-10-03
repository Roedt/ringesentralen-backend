package no.roedt.ringesentralen.brukere

data class TilgangsendringsRequest(var utfoerende: Long, var personMedEndraTilgang: Long) {
    constructor() : this(-1, -1)
}