package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.AggregateRoot
import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.domain.accounts.events.*
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

    fun undelete() {
        this.apply(
            AccountUndeletedEvent(
                id = this.id,
                version = version+1
            )
        )

        return
    }

    fun addExpense(expense: Expense): Account {
        if (expenses.contains(expense)) {
            return this
        }

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
            is AccountUndeletedEvent -> {
                deleted = false
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
