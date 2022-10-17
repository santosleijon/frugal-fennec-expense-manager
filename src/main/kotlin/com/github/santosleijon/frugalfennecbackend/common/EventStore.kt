package com.github.santosleijon.frugalfennecbackend.common

import com.github.santosleijon.frugalfennecbackend.common.eventpubsub.EventServer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class EventStore @Autowired constructor(
    private val eventStoreDAO: EventStoreDAO,
    private val eventServer: EventServer,
) {
    private var logger = LoggerFactory.getLogger(this::class.java)

    fun append(event: DomainEvent) {
        eventStoreDAO.insert(event)
        eventServer.publishEvent(event)

        logger.info("{} recorded on {} with ID {}", event.javaClass.simpleName, event.aggregateName, event.aggregateId)
    }

    fun loadStream(aggregateId: UUID): EventStream {
        val events = eventStoreDAO.findByAggregateId(aggregateId)
        val maxVersion = events.maxByOrNull { it.version }?.version ?: 0

        return EventStream(maxVersion, events)
    }

    data class EventStream(val version: Int, val events: List<DomainEvent>)
}
