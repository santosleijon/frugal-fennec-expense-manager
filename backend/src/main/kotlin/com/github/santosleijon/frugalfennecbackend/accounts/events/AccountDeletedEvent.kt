package com.github.santosleijon.frugalfennecbackend.accounts.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.eventsourcing.DomainEvent
import com.github.santosleijon.frugalfennecbackend.accounts.Account
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class AccountDeletedEvent(
    val id: UUID,
    override val version: Int,
) : DomainEvent {
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateName = Account.aggregateName
    override val aggregateId = id
    override val date: Instant = Instant.now()
}