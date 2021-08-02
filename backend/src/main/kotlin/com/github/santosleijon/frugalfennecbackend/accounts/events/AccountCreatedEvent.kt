package com.github.santosleijon.frugalfennecbackend.accounts.events

import com.github.santosleijon.frugalfennecbackend.IEvent
import com.github.santosleijon.frugalfennecbackend.accounts.AccountType

class AccountCreatedEvent(
    override val aggregateId: Int, // Account number
    val name: String,
    val type: AccountType
) : IEvent