package com.github.santosleijon.frugalfennecbackend.domain.accounts.events

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import java.time.Instant
import java.util.*

class AccountCreatedEvent(
    id: UUID,
    override val version: Int,
    val name: String,
) : DomainEvent {
    override val eventId = id
    override val aggregateName = Account.aggregateName
    override val aggregateId = id
    override val date: Instant = Instant.now()
}