package com.github.santosleijon.financelynxbackend.accounts.events

import com.github.santosleijon.financelynxbackend.IEvent
import java.math.BigDecimal
import java.time.Instant

class AccountCreditedEvent(
    override val aggregateId: Int, // Account number
    val date: Instant,
    val amount: BigDecimal,
    val reason: String
) : IEvent