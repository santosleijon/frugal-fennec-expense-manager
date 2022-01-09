package com.github.santosleijon.frugalfennecbackend.accounts

import com.github.santosleijon.frugalfennecbackend.accounts.application.api.AccountResource
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.toAccountProjection
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

@SpringBootTest
@Testcontainers
internal class AccountResourceTests {

    @Autowired
    lateinit var accountResource: AccountResource

    companion object {
        private val dbImage = ImageFromDockerfile().withFileFromPath(".", Paths.get("./db/."))

        private const val TEST_DB_EXPOSED_PORT = 5432
        private const val TEST_DB_NAME = "frugal_fennec"
        private const val TEST_DB_USER = "frugal_fennec"
        private const val TEST_DB_PASSWORD = "frugal_fennec"

        @Container
        private val dbContainer = (GenericContainer<Nothing>(dbImage) as GenericContainer<*>) // TODO: Create container from docker-compose file (https://www.testcontainers.org/modules/docker_compose/)
            .withExposedPorts(TEST_DB_EXPOSED_PORT)
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
        val newAccount = accountResource.create(AccountResource.CreateAccountInputsDTO(accountName))

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
        val account = accountResource.create(AccountResource.CreateAccountInputsDTO("Original account name"))

        val updatedName = "Updated account name"

        val updateAccountNameInputsDTO = AccountResource.UpdateAccountNameInputsDTO(updatedName)

        val updatedAccount = accountResource.updateName(
            account.id,
            updateAccountNameInputsDTO,
        )

        Assertions.assertThat(account.id).isEqualTo(updatedAccount?.id)
        Assertions.assertThat(updatedAccount?.name).isEqualTo(updatedName)
    }

    @Test
    fun `An account can be deleted`() {
        val createdAccount = accountResource.create(AccountResource.CreateAccountInputsDTO("Account"))
        accountResource.delete(createdAccount.id)

        Assertions.assertThatThrownBy {
            accountResource.get(createdAccount.id)
        }.isInstanceOf(AccountNotFoundError::class.java)
    }

    @Test
    fun `All accounts can be retrieved`() {
        val account1 = accountResource.create(AccountResource.CreateAccountInputsDTO("Account 1"))
        val account2 = accountResource.create(AccountResource.CreateAccountInputsDTO("Account 2"))
        val account3 = accountResource.create(AccountResource.CreateAccountInputsDTO("Account 3"))
        val createdAccounts = listOf(account1, account2, account3).map { it.toAccountProjection() }

        val retrievedAccounts = accountResource.getAll()

        Assertions.assertThat(retrievedAccounts.containsAll(createdAccounts))
    }

    @Test
    fun `Deleted accounts are not included when retrieving all accounts`() {
        val account1 = accountResource.create(AccountResource.CreateAccountInputsDTO("Account 1"))
        val account2 = accountResource.create(AccountResource.CreateAccountInputsDTO("Account 2"))
        val account3 = accountResource.create(AccountResource.CreateAccountInputsDTO("Account 3"))
        accountResource.delete(account1.id)

        val retrievedAccounts = accountResource.getAll()

        Assertions.assertThat(retrievedAccounts.any { it.id == account1.id }).isEqualTo(false)
        Assertions.assertThat(retrievedAccounts.any { it.id == account2.id }).isEqualTo(true)
        Assertions.assertThat(retrievedAccounts.any { it.id == account3.id }).isEqualTo(true)
    }

    @Test
    fun `Add an expense to an account`() {
        val account = accountResource.create(AccountResource.CreateAccountInputsDTO("Account"))

        val testExpense = testExpense()

        accountResource.addExpense(
            id = account.id,
            testExpense.toExpenseInputsDTO(),
        )

        val updatedAccount = accountResource.get(account.id)

        Assertions.assertThat(updatedAccount.expenses).contains(
            Expense(testExpense.date, testExpense.description, testExpense.amount)
        )
    }

    @Test
    fun `Delete an expense from an account`() {
        val account = accountResource.create(AccountResource.CreateAccountInputsDTO("Account"))

        val testExpense = testExpense()

        accountResource.addExpense(
            id = account.id,
            testExpense.toExpenseInputsDTO(),
        )

        val accountBeforeDeletion = accountResource.get(account.id)
        Assertions.assertThat(accountBeforeDeletion.expenses).contains(testExpense)

        accountResource.deleteExpense(
            id = account.id,
            testExpense.toExpenseInputsDTO(),
        )

        val accountAfterDeletion = accountResource.get(account.id)

        Assertions.assertThat(accountAfterDeletion.expenses).doesNotContain(testExpense)
    }

    private fun testExpense() = Expense(
        date = SimpleDateFormat("yyyy-MM-dd").parse("2001-01-01").toInstant(),
        description = "Expense description",
        amount = BigDecimal.valueOf(99.99)
    )

    private fun Expense.toExpenseInputsDTO(): AccountResource.ExpenseInputsDTO {
        return AccountResource.ExpenseInputsDTO(
            date.toString(),
            description,
            amount,
        )
    }
}