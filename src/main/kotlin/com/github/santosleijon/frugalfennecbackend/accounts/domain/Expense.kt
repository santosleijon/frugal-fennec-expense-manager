package com.github.santosleijon.frugalfennecbackend.accounts.domain

import java.math.BigDecimal
import java.time.Instant

data class Expense(
    val date: Instant,
    val description: String,
    val amount: BigDecimal,
)
