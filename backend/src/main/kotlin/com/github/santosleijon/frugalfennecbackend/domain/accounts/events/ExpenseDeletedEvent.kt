package com.github.santosleijon.frugalfennecbackend.domain.accounts.events

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Expense
import java.time.Instant
import java.util.*

class ExpenseDeletedEvent(
    accountId: UUID,
    override val version: Int,
    val expense: Expense,
) : DomainEvent {
    override val eventId: UUID = UUID.randomUUID()
    override val aggregateName = Account.aggregateName
    override val aggregateId = accountId
    override val date: Instant = Instant.now()
}