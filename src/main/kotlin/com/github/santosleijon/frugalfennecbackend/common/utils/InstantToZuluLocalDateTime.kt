package com.github.santosleijon.frugalfennecbackend.common.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Instant.toZuluLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Z"))
