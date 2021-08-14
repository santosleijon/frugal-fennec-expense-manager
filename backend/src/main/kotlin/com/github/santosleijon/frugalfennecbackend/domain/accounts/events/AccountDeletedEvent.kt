package com.github.santosleijon.frugalfennecbackend.domain.accounts.events

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import java.time.Instant
import java.util.*

class AccountDeletedEvent(
    id: UUID,
    override val version: Int,
) : DomainEvent {
    override val type: DomainEvent.EventType = DomainEvent.EventType.AccountDeleted
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateId = id
    override val date: Instant = Instant.now()
}