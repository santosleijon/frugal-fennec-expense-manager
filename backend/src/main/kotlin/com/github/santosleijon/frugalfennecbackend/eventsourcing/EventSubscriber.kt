package com.github.santosleijon.frugalfennecbackend.eventsourcing

interface EventSubscriber {
    fun handleEvent(event: DomainEvent)
}