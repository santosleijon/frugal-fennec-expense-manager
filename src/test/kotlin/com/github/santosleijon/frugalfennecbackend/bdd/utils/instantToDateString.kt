package com.github.santosleijon.frugalfennecbackend.bdd.utils

import java.time.Instant
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

fun Instant.toDateString(): String {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(UTC)
        .format(this)
}
