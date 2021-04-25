package com.github.santosleijon.financelynxbackend.accounts.events

import com.github.santosleijon.financelynxbackend.IEvent
import com.github.santosleijon.financelynxbackend.accounts.AccountType

class AccountCreatedEvent(
    override val aggregateId: Int, // Account number
    val name: String,
    val type: AccountType
) : IEvent