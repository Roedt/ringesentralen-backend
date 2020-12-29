package no.roedt.ringesentralen.brukere

import no.roedt.ringesentralen.samtale.GroupID

data class Brukerendring(val personID: Long, val nyGroupId: GroupID)