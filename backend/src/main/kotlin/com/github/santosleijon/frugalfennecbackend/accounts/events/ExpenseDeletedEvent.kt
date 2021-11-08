package com.github.santosleijon.frugalfennecbackend.accounts.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.eventsourcing.DomainEvent
import com.github.santosleijon.frugalfennecbackend.accounts.Account
import com.github.santosleijon.frugalfennecbackend.accounts.Expense
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class ExpenseDeletedEvent(
    val accountId: UUID,
    override val version: Int,
    val expense: Expense,
) : DomainEvent {
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateName = Account.aggregateName
    override val aggregateId = accountId
    override val date: Instant = Instant.now()
}