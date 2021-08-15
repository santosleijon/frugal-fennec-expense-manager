package com.github.santosleijon.frugalfennecbackend.domain.accounts.events

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import java.time.Instant
import java.util.*

class AccountDeletedEvent(
    id: UUID,
    override val version: Int,
) : DomainEvent {
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateName = Account.aggregateName
    override val aggregateId = id
    override val date: Instant = Instant.now()
}