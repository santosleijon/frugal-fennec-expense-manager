package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.accounts.errors.AccountNotFoundError
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant

@SpringBootTest
internal class AccountResourceTests {
    @Autowired
    lateinit var accountResource: AccountResource

    @Test
    fun `A new account can be created and retrieved`() {
        val accountName = "Test account"
        val newAccount = accountResource.create(accountName)

        Assertions.assertThat(newAccount).isNotNull
        Assertions.assertThat(newAccount.id).isNotNull
        Assertions.assertThat(newAccount.name).isEqualTo(accountName)

        val foundAccount = accountResource.get(newAccount.id)

        Assertions.assertThat(foundAccount).isNotNull
        Assertions.assertThat(foundAccount.id).isNotNull
        Assertions.assertThat(foundAccount.name).isEqualTo(accountName)
    }

    @Test
    fun `An account name can be updated`() {
        val account = accountResource.create("Original account name")

        val updatedName = "Updated account name"
        val updatedAccount = accountResource.updateName(account.id, updatedName)

        Assertions.assertThat(account.id).isEqualTo(updatedAccount?.id)
        Assertions.assertThat(updatedAccount?.name).isEqualTo(updatedName)
    }

    @Test
    fun `An account can be deleted`() {
        val createdAccount = accountResource.create("Account")
        accountResource.delete(createdAccount.id)

        Assertions.assertThatThrownBy {
            accountResource.get(createdAccount.id)
        }.isInstanceOf(AccountNotFoundError::class.java)
    }

    @Test
    fun `All accounts can be retrieved`() {
        val account1 = accountResource.create("Account 1")
        val account2 = accountResource.create("Account 2")
        val account3 = accountResource.create("Account 3")
        val createdAccounts = listOf(account1, account2, account3)

        val retrievedAccounts = accountResource.getAll()

        Assertions.assertThat(retrievedAccounts.containsAll(createdAccounts))
    }

    @Test
    fun `Deleted accounts are not included when retrieving all accounts`() {
        val account1 = accountResource.create("Account 1")
        val account2 = accountResource.create("Account 2")
        val account3 = accountResource.create("Account 3")
        accountResource.delete(account1.id)

        val retrievedAccounts = accountResource.getAll()

        Assertions.assertThat(retrievedAccounts.any { it.id == account1.id }).isEqualTo(false)
        Assertions.assertThat(retrievedAccounts.any { it.id == account2.id }).isEqualTo(true)
        Assertions.assertThat(retrievedAccounts.any { it.id == account3.id }).isEqualTo(true)
    }

    @Test
    fun `Add an expense to an account`() {
        val account = accountResource.create("Account")

        val date = Instant.now()
        val description = "Expense description"
        val amount = BigDecimal.valueOf(99.99)

        accountResource.addExpense(
            id = account.id,
            date = date,
            description = description,
            amount = amount,
        )

        val updatedAccount = accountResource.get(account.id)

        Assertions.assertThat(updatedAccount.expenses).contains(
            Expense(account.id, date, description, amount)
        )
    }
}