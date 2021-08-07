package com.github.santosleijon.frugalfennecbackend.domain

import org.springframework.stereotype.Component
import java.util.*

@Component
class EventStore {
    private val inMemoryEventStore: MutableSet<DomainEvent> = emptySet<DomainEvent>().toMutableSet()

    fun append(event: DomainEvent) {
        inMemoryEventStore.add(event)
    }

    fun loadStream(aggregateId: UUID): EventStream {
        val events = inMemoryEventStore.filter { it.aggregateId == aggregateId }.toSet()
        val maxVersion = events.maxByOrNull { it.version }?.version ?: 0

        return EventStream(maxVersion, events)
    }

    data class EventStream(val version: Int, val events: Set<DomainEvent>)
}