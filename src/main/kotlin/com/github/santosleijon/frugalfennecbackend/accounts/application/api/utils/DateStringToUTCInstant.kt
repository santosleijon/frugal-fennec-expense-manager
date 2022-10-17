package com.github.santosleijon.frugalfennecbackend.accounts.application.api.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun String.toUTCInstant(): Instant {
    val datePattern = Regex("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$")
    val tenDigitValue = this.substring(0, 10)

    if (!(tenDigitValue matches datePattern)) {
        throw IllegalArgumentException("String must be valid date formatted as yyyy-MM-dd")
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse("$tenDigitValue 00:00:00", formatter).toInstant(ZoneOffset.UTC)
}
