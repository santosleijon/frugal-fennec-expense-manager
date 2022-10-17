package com.github.santosleijon.frugalfennecbackend.accounts.domain.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class AccountCreatedEvent(
    val id: UUID,
    override val version: Int,
    val name: String,
    override val userId: UUID,
) : DomainEvent {
    override val eventId = id
    override val aggregateName = Account.aggregateName
    override val aggregateId = id
    override val date: Instant = Instant.now()
}
