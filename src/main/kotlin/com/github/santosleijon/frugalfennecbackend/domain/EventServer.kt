package com.github.santosleijon.frugalfennecbackend.domain

interface EventServer {
    fun publishEvent(event: DomainEvent)
}
