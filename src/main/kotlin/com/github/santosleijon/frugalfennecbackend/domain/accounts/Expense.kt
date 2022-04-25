package com.github.santosleijon.frugalfennecbackend.domain.accounts

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant

data class Expense(
    val date: Instant,
    val description: String,
    val amount: BigDecimal,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other is Expense &&
                this.date == other.date &&
                this.description == other.description &&
                this.amount.setScale(2, RoundingMode.HALF_EVEN) == other.amount.setScale(2, RoundingMode.HALF_EVEN)) {
            return true
        }

        return false
    }

    override fun hashCode(): Int {
        var result = date.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + amount.hashCode()
        return result
    }
}
