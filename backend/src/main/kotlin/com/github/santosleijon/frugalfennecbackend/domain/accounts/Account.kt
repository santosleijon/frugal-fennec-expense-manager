package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.AggregateRoot
import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.domain.accounts.events.AccountCreatedEvent
import java.util.*

class Account(
    version: Int
) : AggregateRoot(version) {
    var id: UUID? = null
        private set
    var name: String? = null
        private set

    companion object {
        fun loadFrom(version: Int, events: Set<DomainEvent>): Account {
            val newAccount = Account(version)

            for (event in events) {
                newAccount.mutate(event)
            }

            return newAccount
        }
    }

    constructor(
        id: UUID,
        name: String
    ) : this(version = 0) {
        this.apply(
            AccountCreatedEvent(
                id,
                version,
                name,
            )
        )
    }

    override fun mutate(event: DomainEvent) {
        when (event) {
            is AccountCreatedEvent -> {
                id = event.aggregateId
                name = event.name
            }
        }
    }
}