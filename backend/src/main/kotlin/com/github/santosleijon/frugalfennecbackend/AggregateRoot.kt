package com.github.santosleijon.frugalfennecbackend

import java.util.*

abstract class AggregateRoot(
    val id: UUID,
    val version: Int,
) {
    val pendingEvents = mutableListOf<DomainEvent>()

    fun apply(event: DomainEvent) {
        pendingEvents.add(event)
        mutate(event)
    }

    abstract fun mutate(event: DomainEvent)
}