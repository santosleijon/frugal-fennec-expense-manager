package com.github.santosleijon.frugalfennecbackend.accounts.domain.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.eventsourcing.DomainEvent
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class AccountNameUpdatedEvent(
    val id: UUID,
    override val version: Int,
    val newName: String,
) : DomainEvent {
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateName = Account.aggregateName
    override val aggregateId = id
    override val date: Instant = Instant.now()
}