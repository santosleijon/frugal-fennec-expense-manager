package com.github.santosleijon.frugalfennecbackend.accounts.domain

import com.github.santosleijon.frugalfennecbackend.accounts.domain.events.*
import com.github.santosleijon.frugalfennecbackend.common.AggregateRoot
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import java.util.*

class Account(
    id: UUID,
    version: Int,
) : AggregateRoot(id, version) {
    var name: String? = null
        private set

    var userId: UUID? = null
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
        name: String,
        userId: UUID,
    ) : this(id = id, version = 0) {
        this.apply(
            AccountCreatedEvent(
                id,
                version,
                name,
                userId,
            )
        )
    }

    fun setName(newName: String, actorUserId: UUID): Account {
        this.apply(
            AccountNameUpdatedEvent(
                id = id,
                version = version+1,
                newName = newName,
                actorUserId
            )
        )

        return this
    }

    fun delete(actorUserId: UUID) {
        this.apply(
            AccountDeletedEvent(
                id = this.id,
                version = version+1,
                actorUserId,
            )
        )

        return
    }

    fun undelete(actorUserId: UUID) {
        this.apply(
            AccountUndeletedEvent(
                id = this.id,
                version = version+1,
                userId = actorUserId,
            )
        )

        return
    }

    fun addExpense(expense: Expense, actorUserId: UUID): Account {
        if (expenses.contains(expense)) {
            return this
        }

        this.apply(
            ExpenseAddedEvent(
                accountId = this.id,
                version = version+1,
                expense = expense,
                userId = actorUserId,
            )
        )

        return this
    }

    fun deleteExpense(expense: Expense, actorUserId: UUID): Account {
        this.apply(
            ExpenseDeletedEvent(
                accountId = this.id,
                version = version+1,
                expense = expense,
                userId = actorUserId,
            )
        )

        return this
    }

    override fun mutate(event: DomainEvent) {
        when (event) {
            is AccountCreatedEvent -> {
                name = event.name
                userId = event.userId
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
