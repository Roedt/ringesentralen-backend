package no.roedt.ringesentralen.brukere

import UserId

data class AutentisertTilgangsendringRequest(val userId: UserId, val tilgangsendringRequest: TilgangsendringsRequest) {

    fun personMedEndraTilgang() = tilgangsendringRequest.personMedEndraTilgang
}