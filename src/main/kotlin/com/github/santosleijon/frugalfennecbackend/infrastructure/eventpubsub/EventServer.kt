package com.github.santosleijon.frugalfennecbackend.infrastructure.eventpubsub

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import org.springframework.stereotype.Component

@Component
class EventServer {
    private val eventHandlers: MutableMap<String, List<(DomainEvent) -> Unit>> =
        emptyMap<String, List<(DomainEvent) -> Unit>>().toMutableMap()

    fun registerEventHandler(aggregateName: String, eventHandler: (DomainEvent) -> Unit) {
        val currentSubscribers = eventHandlers.getOrElse(aggregateName) { emptyList() }
        eventHandlers[aggregateName] = currentSubscribers.plus(eventHandler)
    }

    fun deregisterEventHandler(aggregateName: String, eventHandler: (DomainEvent) -> Unit) {
        val currentSubscribers = eventHandlers.getOrElse(aggregateName) { emptyList() }
        eventHandlers[aggregateName] = currentSubscribers.minus(eventHandler)
    }

    fun publishEvent(event: DomainEvent) {
        val eventHandlers = eventHandlers[event.aggregateName]
            ?: return

        eventHandlers.forEach { handleEvent ->
            handleEvent(event)
        }
    }
}
