package com.github.santosleijon.frugalfennecbackend

import com.github.santosleijon.frugalfennecbackend.accounts.dao.EventStoreDAO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class EventStore @Autowired constructor(
    private val eventStoreDAO: EventStoreDAO,
) {
    /***
     * TODO: Update projections when events are appended
     *
     * topic = DomainEvent.aggregateName
     *
     * @Singleton
     * EventServer
     *      - private val subscriberList: Map<String, List<EventSubscriber>> = emptyList().toMutableList()
     *      - registerSubscriber(topic: String, subscriber: EventSubscriber)
     *      - publishEvent(topic: String, event: DomainEvent)
     *          - val subscribers = subscriberList.get(event type)
     *          - subscribers.forEach { it.handleEvent(topic, event) }
     *
     * EventSubscriber
     *      - constructor(topics: List<String>)
     *            - eventServer.registerSubscriber(this, topic)
     *      - fun handleEvent(topic: String, event: DomainEvent)
     *            - update DB etc...
     *
     * EventPublisher
     *      - constructor(topic: String)
     *      - publish(event)
     *          - eventServer.publishEvent(this.topic, event)
     *
     *
     * EventStore.append()
     *      - eventServer.publishEvent(event.aggregateName, event)
     */

    fun append(event: DomainEvent) {
        eventStoreDAO.add(event)
    }

    fun loadStream(aggregateId: UUID): EventStream {
        val events = eventStoreDAO.findAll().filter { it.aggregateId == aggregateId }
        val maxVersion = events.maxByOrNull { it.version }?.version ?: 0

        return EventStream(maxVersion, events)
    }

    // TODO: Replace with aggregate projections
    fun loadStreamsByAggregateName(aggregateName: String): List<EventStream> {
        val aggregateEvents = eventStoreDAO.findByAggregateName(aggregateName)

        if (aggregateEvents.isEmpty()) {
            return emptyList()
        }

        return aggregateEvents
            .groupBy { it.aggregateId }
            .map { (_, events) -> EventStream(
                version = events.maxByOrNull { it.version }?.version ?: 0,
                events = events
            ) }
    }

    data class EventStream(val version: Int, val events: List<DomainEvent>)
}
