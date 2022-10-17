package com.github.santosleijon.frugalfennecbackend.common.eventpubsub

import com.github.santosleijon.frugalfennecbackend.common.DomainEvent

abstract class EventSubscriber constructor(
    aggregateName: String,
    eventServer: EventServer,
) {

    init {
        eventServer.registerEventHandler(aggregateName, ::handleEvent)
    }

    abstract fun handleEvent(event: DomainEvent)
}
