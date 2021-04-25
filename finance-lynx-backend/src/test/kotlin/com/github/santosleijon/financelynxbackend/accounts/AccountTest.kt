package com.github.santosleijon.financelynxbackend.accounts

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class AccountTest {
    @Test
    fun `Debit account should be able to be created`() {
        // Arrange and Act
        val account = createDebitAccount()

        // Assert
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `Debit account should be able to be debited after creation`() {
        // Arrange
        val account = createDebitAccount()

        // Act
        account.debit(
            amount = BigDecimal.valueOf(100),
            reason = "Test debit"
        )

        // Assert
        assertThat(account.balance).isEqualTo(BigDecimal.valueOf(100))
    }

    @Test
    fun `Credit account should be able to be credited after creation`() {
        // Arrange
        val account = createCreditAccount()

        // Act
        account.credit(
            amount = BigDecimal.valueOf(100),
            reason = "Test credit"
        )

        // Assert
        assertThat(account.balance).isEqualTo(BigDecimal.valueOf(-100))
    }

    @Test
    fun `Negative amounts should not be able to be debited from debit account`() {
        // Arrange
        val account = createDebitAccount()

        // Act and Assert
        assertThrows(Error::class.java) {
            account.credit(
                amount = BigDecimal.valueOf(-100),
                reason = "Test debit"
            )
        }
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `Negative amounts should not be able to be credited from debit account`() {
        // Arrange
        val account = createDebitAccount()

        // Act and Assert
        assertThrows(Error::class.java) {
            account.debit(
                amount = BigDecimal.valueOf(-100),
                reason = "Test debit"
            )
        }
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `Negative amounts should not be able to be debited from credit account`() {
        // Arrange
        val account = createCreditAccount()

        // Act and Assert
        assertThrows(Error::class.java) {
            account.debit(
                amount = BigDecimal.valueOf(-100),
                reason = "Test debit"
            )
        }
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
    }

    @Test
    fun `Negative amount should not be able to be credited from credit account`() {
        // Arrange
        val account = createCreditAccount()

        // Act and Assert
        assertThrows(Error::class.java) {
            account.credit(
                amount = BigDecimal.valueOf(-100),
                reason = "Test credit"
            )
        }
        assertThat(account.balance).isEqualTo(BigDecimal.ZERO)
    }

    private fun createDebitAccount(): Account {
        val account = Account(1000)
        account.create(
            name = "Test Debit Account",
            type = AccountType.DEBIT
        )
        return account
    }

    private fun createCreditAccount(): Account {
        val account = Account(3000)
        account.create(
            name = "Test Credit Account",
            type = AccountType.CREDIT
        )
        return account
    }
}