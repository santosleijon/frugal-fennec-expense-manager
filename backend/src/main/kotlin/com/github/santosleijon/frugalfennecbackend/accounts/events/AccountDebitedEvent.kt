package com.github.santosleijon.frugalfennecbackend.accounts.events

import com.github.santosleijon.frugalfennecbackend.IEvent
import java.math.BigDecimal
import java.time.Instant

class AccountDebitedEvent(
    override val aggregateId: Int, // Account number
    val date: Instant,
    val amount: BigDecimal,
    val reason: String
) : IEvent