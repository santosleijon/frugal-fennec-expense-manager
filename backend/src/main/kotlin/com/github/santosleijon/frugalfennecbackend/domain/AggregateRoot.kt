package com.github.santosleijon.frugalfennecbackend.domain

abstract class AggregateRoot(
    val version: Int
) {
    val pendingEvents = mutableListOf<DomainEvent>()

    fun apply(event: DomainEvent) {
        pendingEvents.add(event)
        mutate(event)
    }

    abstract fun mutate(event: DomainEvent)
}