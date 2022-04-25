package com.github.santosleijon.frugalfennecbackend.domain

interface EventSubscriber {
    fun handleEvent(event: DomainEvent)
}