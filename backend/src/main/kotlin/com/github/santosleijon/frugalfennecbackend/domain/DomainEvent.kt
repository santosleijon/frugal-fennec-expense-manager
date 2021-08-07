package com.github.santosleijon.frugalfennecbackend.domain

import java.time.Instant
import java.util.*

interface DomainEvent {
    val eventId: UUID
    val aggregateId: UUID
    val date: Instant
    val version: Int
    val type: EventType

    enum class EventType {
        AccountCreated
    }
}
