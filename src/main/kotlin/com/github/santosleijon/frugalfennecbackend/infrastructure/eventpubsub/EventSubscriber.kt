package com.github.santosleijon.frugalfennecbackend.infrastructure.eventpubsub

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent

interface EventSubscriber {
    fun handleEvent(event: DomainEvent)
}