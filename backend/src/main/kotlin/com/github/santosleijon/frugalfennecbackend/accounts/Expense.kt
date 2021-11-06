package com.github.santosleijon.frugalfennecbackend.accounts

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class Expense(
    val accountId: UUID,
    val date: Instant,
    val description: String,
    val amount: BigDecimal,
)
