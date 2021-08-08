package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.AggregateRoot
import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.domain.accounts.events.AccountCreatedEvent
import com.github.santosleijon.frugalfennecbackend.domain.accounts.events.AccountNameUpdatedEvent
import java.util.*

class Account(
    id: UUID,
    version: Int,
) : AggregateRoot(id, version) {
    var name: String? = null
        private set

    companion object {
        fun loadFrom(events: Set<DomainEvent>, id: UUID, version: Int): Account {
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

    override fun mutate(event: DomainEvent) {
        when (event) {
            is AccountCreatedEvent -> {
                name = event.name
            }
            is AccountNameUpdatedEvent -> {
                name = event.newName
            }
        }
    }
}