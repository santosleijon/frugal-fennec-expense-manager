package com.github.santosleijon.frugalfennecbackend.infrastructure.eventpubsub

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent

abstract class EventSubscriber constructor(
    aggregateName: String,
    eventServer: EventServer,
) {

    init {
        eventServer.registerEventHandler(aggregateName, ::handleEvent)
    }

    abstract fun handleEvent(event: DomainEvent)
}
