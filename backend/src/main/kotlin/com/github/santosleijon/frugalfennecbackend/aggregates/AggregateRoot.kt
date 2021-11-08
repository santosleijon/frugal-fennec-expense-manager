package com.github.santosleijon.frugalfennecbackend.aggregates

import com.github.santosleijon.frugalfennecbackend.eventsourcing.DomainEvent
import java.util.*

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