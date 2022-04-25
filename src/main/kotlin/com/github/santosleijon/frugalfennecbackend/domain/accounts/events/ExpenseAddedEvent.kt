package com.github.santosleijon.frugalfennecbackend.domain.accounts.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Expense
import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class ExpenseAddedEvent(
    val accountId: UUID,
    override val version: Int,
    val expense: Expense,
) : DomainEvent {
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateName = Account.aggregateName
    override val aggregateId = accountId
    override val date: Instant = Instant.now()
}
