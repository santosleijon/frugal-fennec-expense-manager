package com.github.santosleijon.frugalfennecbackend.domain

import org.springframework.stereotype.Component
import java.util.*

@Component
class EventStore {
    private val inMemoryEventStore: MutableList<DomainEvent> = emptyList<DomainEvent>().toMutableList()

    fun append(event: DomainEvent) {
        inMemoryEventStore.add(event)
    }

    fun loadStream(aggregateId: UUID): EventStream {
        val events = inMemoryEventStore.filter { it.aggregateId == aggregateId }
        val maxVersion = events.maxByOrNull { it.version }?.version ?: 0

        return EventStream(maxVersion, events)
    }

    // TODO: Replace with aggregate projections
    fun loadStreamsByAggregate(aggregateName: String): List<EventStream> {
        val aggregateEvents = inMemoryEventStore.filter { it.aggregateName == aggregateName }
        return aggregateEvents
            .groupBy { it.aggregateId }
            .map { (_, events) -> EventStream(
                version = events.maxByOrNull { it.version }?.version ?: 0,
                events = events
            ) }
    }

    data class EventStream(val version: Int, val events: List<DomainEvent>)
}