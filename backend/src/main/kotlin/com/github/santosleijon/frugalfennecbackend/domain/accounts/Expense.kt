package com.github.santosleijon.frugalfennecbackend.domain.accounts

import java.math.BigDecimal
import java.time.Instant

data class Expense(
    val account: Account,
    val date: Instant,
    val description: String,
    val amount: BigDecimal,
)
