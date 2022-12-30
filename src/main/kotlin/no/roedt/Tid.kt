package no.roedt

import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun tidssone(): ZoneId = ZoneId.of("UTC")

fun norskTid(): ZoneId = ZoneId.of("Europe/Oslo")

fun ZonedDateTime.formater(): String = tilNorskTid().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"))

fun ZonedDateTime.tilNorskTid(): ZonedDateTime = plusSeconds(toInstant().tidsforskjellISekunder())

fun Timestamp.skrivUt() = toInstant().atZone(norskTid()).formater()

fun Instant.tilNorskTid(): Instant = plusSeconds(tidsforskjellISekunder())

private fun Instant.tidsforskjellISekunder(): Long = norskTid().rules.getOffset(this).totalSeconds.toLong()
