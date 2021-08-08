package com.github.santosleijon.frugalfennecbackend.domain.accounts.events

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import java.time.Instant
import java.util.*

class AccountNameUpdatedEvent(
    id: UUID,
    override val version: Int,
    val newName: String,
) : DomainEvent {
    override val type: DomainEvent.EventType = DomainEvent.EventType.AccountCreated
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateId = id
    override val date: Instant = Instant.now()
}