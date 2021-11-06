package com.github.santosleijon.frugalfennecbackend

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Instant
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class",
)
interface DomainEvent {
    val eventId: UUID
    val aggregateName: String
    val aggregateId: UUID
    val date: Instant
    val version: Int
}
