package com.github.santosleijon.frugalfennecbackend.accounts.domain

import com.github.santosleijon.frugalfennecbackend.accounts.domain.events.*
import com.github.santosleijon.frugalfennecbackend.aggregates.AggregateRoot
import com.github.santosleijon.frugalfennecbackend.eventsourcing.DomainEvent
import java.util.*

class Account(
    id: UUID,
    version: Int,
) : AggregateRoot(id, version) {
    var name: String? = null
        private set

    var deleted: Boolean = false
        private set

    var expenses: MutableList<Expense> = emptyList<Expense>().toMutableList()
        private set

    companion object {
        const val aggregateName: String = "Account"

        fun loadFrom(events: List<DomainEvent>, id: UUID, version: Int): Account {
            val newAccount = Account(id, version)

            for (event in events) {
                newAccount.mutate(event)
            }

            return newAccount
        }
    }

    constructor(
        id: UUID,
        name: String
    ) : this(id = id, version = 0) {
        this.apply(
            AccountCreatedEvent(
                id,
                version,
                name,
            )
        )
    }

    fun setName(newName: String): Account {
        this.apply(
            AccountNameUpdatedEvent(
                id = id,
                version = version+1,
                newName = newName
            )
        )

        return this
    }

    fun delete() {
        this.apply(
            AccountDeletedEvent(
                id = this.id,
                version = version+1
            )
        )

        return
    }

    fun addExpense(expense: Expense): Account {
        this.apply(
            ExpenseAddedEvent(
                accountId = this.id,
                version = version+1,
                expense = expense,
            )
        )

        return this
    }

    fun deleteExpense(expense: Expense): Account {
        this.apply(
            ExpenseDeletedEvent(
                accountId = this.id,
                version = version+1,
                expense = expense,
            )
        )

        return this
    }

    override fun mutate(event: DomainEvent) {
        when (event) {
            is AccountCreatedEvent -> {
                name = event.name
            }
            is AccountNameUpdatedEvent -> {
                name = event.newName
            }
            is AccountDeletedEvent -> {
                deleted = true
            }
            is ExpenseAddedEvent -> {
                expenses.add(event.expense)
            }
            is ExpenseDeletedEvent -> {
                expenses.remove(event.expense)
            }
        }
    }
}