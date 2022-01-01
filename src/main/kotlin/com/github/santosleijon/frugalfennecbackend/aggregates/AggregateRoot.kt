package com.github.santosleijon.frugalfennecbackend.aggregates

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.github.santosleijon.frugalfennecbackend.eventsourcing.DomainEvent
import java.util.*

@JsonIgnoreProperties("version", "pendingEvents")
abstract class AggregateRoot(
    val id: UUID,
    val version: Int,
) {
    val pendingEvents = mutableListOf<DomainEvent>()

    fun apply(event: DomainEvent) {
        pendingEvents.add(event)
        mutate(event)
    }

    abstract fun mutate(event: DomainEvent)
}