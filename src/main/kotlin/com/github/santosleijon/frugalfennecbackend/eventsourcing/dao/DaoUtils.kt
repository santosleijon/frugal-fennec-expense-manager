package com.github.santosleijon.frugalfennecbackend.eventsourcing.dao

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Instant.toZuluLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Z"))
