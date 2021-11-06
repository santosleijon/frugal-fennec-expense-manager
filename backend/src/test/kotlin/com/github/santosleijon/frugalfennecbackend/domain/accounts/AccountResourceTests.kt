package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.accounts.AccountResource
import com.github.santosleijon.frugalfennecbackend.accounts.Expense
import com.github.santosleijon.frugalfennecbackend.accounts.errors.AccountNotFoundError
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

@SpringBootTest
@Testcontainers
internal class AccountResourceTests {

    @Autowired
    lateinit var accountResource: AccountResource

    companion object {
        private val dbImage = ImageFromDockerfile().withFileFromPath(".", Paths.get("./db/."))

        private const val TEST_DB_NAME = "frugal_fennec"
        private const val TEST_DB_USER = "frugal_fennec"
        private const val TEST_DB_PASSWORD = "frugal_fennec"

        @Container
        private val dbContainer = (GenericContainer<Nothing>(dbImage) as GenericContainer<*>)
            .withExposedPorts(5432)
            .waitingFor(
                Wait.forLogMessage(".*PostgreSQL init process complete; ready for start up.*\\n", 1)
            )

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { "jdbc:postgresql://" + dbContainer.host + ":" + dbContainer.firstMappedPort + "/" + TEST_DB_NAME }
            registry.add("spring.datasource.username") { TEST_DB_USER }
            registry.add("spring.datasource.password") { TEST_DB_PASSWORD }
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }
    }

    @Test
    fun `A new account can be created and retrieved`() {
        val accountName = "Test account"
        val newAccount = accountResource.create(AccountResource.CreateAccountCommandInputsDTO(accountName))

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
        val account = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Original account name"))

        val updatedName = "Updated account name"
        val updatedAccount = accountResource.updateName(account.id, updatedName)

        Assertions.assertThat(account.id).isEqualTo(updatedAccount?.id)
        Assertions.assertThat(updatedAccount?.name).isEqualTo(updatedName)
    }

    @Test
    fun `An account can be deleted`() {
        val createdAccount = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account"))
        accountResource.delete(createdAccount.id)

        Assertions.assertThatThrownBy {
            accountResource.get(createdAccount.id)
        }.isInstanceOf(AccountNotFoundError::class.java)
    }

    @Test
    fun `All accounts can be retrieved`() {
        val account1 = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account 1"))
        val account2 = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account 2"))
        val account3 = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account 3"))
        val createdAccounts = listOf(account1, account2, account3)

        val retrievedAccounts = accountResource.getAll()

        Assertions.assertThat(retrievedAccounts.containsAll(createdAccounts))
    }

    @Test
    fun `Deleted accounts are not included when retrieving all accounts`() {
        val account1 = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account 1"))
        val account2 = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account 2"))
        val account3 = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account 3"))
        accountResource.delete(account1.id)

        val retrievedAccounts = accountResource.getAll()

        Assertions.assertThat(retrievedAccounts.any { it.id == account1.id }).isEqualTo(false)
        Assertions.assertThat(retrievedAccounts.any { it.id == account2.id }).isEqualTo(true)
        Assertions.assertThat(retrievedAccounts.any { it.id == account3.id }).isEqualTo(true)
    }

    @Test
    fun `Add an expense to an account`() {
        val account = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account"))

        val testExpense = testExpense(account.id)

        accountResource.addExpense(
            id = account.id,
            date = testExpense.date,
            description = testExpense.description,
            amount = testExpense.amount,
        )

        val updatedAccount = accountResource.get(account.id)

        Assertions.assertThat(updatedAccount.expenses).contains(
            Expense(account.id, testExpense.date, testExpense.description, testExpense.amount)
        )
    }

    @Test
    fun `Delete an expense from an account`() {
        val account = accountResource.create(AccountResource.CreateAccountCommandInputsDTO("Account"))

        val testExpense = testExpense(account.id)

        accountResource.addExpense(
            id = account.id,
            date = testExpense.date,
            description = testExpense.description,
            amount = testExpense.amount,
        )

        val accountBeforeDeletion = accountResource.get(account.id)
        Assertions.assertThat(accountBeforeDeletion.expenses).contains(testExpense)

        accountResource.deleteExpense(
            id = account.id,
            date = testExpense.date,
            description = testExpense.description,
            amount = testExpense.amount,
        )

        val accountAfterDeletion = accountResource.get(account.id)

        Assertions.assertThat(accountAfterDeletion.expenses).doesNotContain(testExpense)
    }

    private fun testExpense(accountId: UUID) = Expense(
        accountId = accountId,
        date = SimpleDateFormat("yyyy-MM-dd").parse("2001-01-01").toInstant(),
        description = "Expense description",
        amount = BigDecimal.valueOf(99.99)
    )
}