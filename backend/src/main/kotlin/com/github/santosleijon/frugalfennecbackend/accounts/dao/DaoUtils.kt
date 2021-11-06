package com.github.santosleijon.frugalfennecbackend.accounts.dao

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

fun Instant.toZuluLocalDate(): LocalDate = LocalDate.ofInstant(this, ZoneId.of("Z"))
