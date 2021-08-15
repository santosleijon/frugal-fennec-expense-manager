package com.github.santosleijon.frugalfennecbackend.domain

import java.time.Instant
import java.util.*

interface DomainEvent {
    val eventId: UUID
    val aggregateName: String
    val aggregateId: UUID
    val date: Instant
    val version: Int
}
