package com.github.santosleijon.frugalfennecbackend.steps

import com.github.santosleijon.frugalfennecbackend.TestDbContainer
import com.github.santosleijon.frugalfennecbackend.accounts.application.api.AccountResource
import com.github.santosleijon.frugalfennecbackend.accounts.application.api.toUTCInstant
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import java.math.BigDecimal
import java.time.Instant

class AccountsSteps {

    // TODO: Move to separate class that can be inherited to this one
    companion object {
        private val dbContainer = TestDbContainer()

        @Suppress("unused")
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { dbContainer.dbUrl }
            registry.add("spring.datasource.username") { dbContainer.dbUsername }
            registry.add("spring.datasource.password") { dbContainer.dbPassword }
            registry.add("spring.jpa.hibernate.ddl-auto") { "create-drop" }
        }
    }

    @Autowired
    private lateinit var accountResource: AccountResource

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountProjectionRepository: AccountProjectionRepository

    private var retrievedAccounts: List<AccountProjection> = emptyList()

    @Given("an account with the name {string}")
    fun givenAnAccount(accountName: String) {
        accountResource.create(AccountResource.CreateAccountInputsDTO(accountName))
    }

    @Given("an account with the name {string} and the following expenses:")
    fun givenAnAccountWithExpenses(accountName: String, expenses: List<Expense>) {
        givenAnAccount(accountName)
        addExpenses(accountName, expenses)
    }

    @When("an account is created with the name {string}")
    fun createAccount(accountName: String) {
        accountResource.create(AccountResource.CreateAccountInputsDTO(accountName))
    }

    @When("account {string} is renamed to {string}")
    fun updateAccountName(oldName: String, newName: String) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(oldName)
        val account = accountRepository.findByIdOrNull(accountProjection!!.id)!!
        account.setName(newName)
        accountRepository.save(account)
    }

    @When("the account with the name {string} is deleted")
    fun deleteAccount(accountName: String) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(accountName)
        val account = accountRepository.findByIdOrNull(accountProjection!!.id)!!
        account.delete()
        accountRepository.save(account)
    }

    @When("all accounts are retrieved")
    fun retrieveAllAccounts() {
        retrievedAccounts = accountProjectionRepository.findAll()
    }

    @When("the following expenses are added to the account with the name {string}:")
    fun addExpenses(accountName: String, expenses: List<Expense>) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(accountName)
        val account = accountRepository.findByIdOrNull(accountProjection!!.id)!!

        expenses.forEach { expense ->
            account.addExpense(expense)
        }

        accountRepository.save(account)
    }

    @When("the following expenses are deleted from the account with the name {string}:")
    fun deleteExpenses(accountName: String, expenses: List<Expense>) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(accountName)
        val account = accountRepository.findByIdOrNull(accountProjection!!.id)!!

        expenses.forEach { expense ->
            account.deleteExpense(expense)
        }

        accountRepository.save(account)
    }

    @Then("an account with the name {string} exists")
    fun assertAccountExists(accountName: String) {
        val account = accountProjectionRepository.findByNameOrNull(accountName)
        Assertions.assertThat(account).isNotNull
    }

    @Then("an account with the name {string} does not exist")
    fun assertAccountDoesNotExist(accountName: String) {
        val account = accountProjectionRepository.findByNameOrNull(accountName)
        Assertions.assertThat(account).isNull()
    }

    @Then("the retrieved list of accounts contains the following account names:")
    fun assertRetrievedAccountsContain(accountNames: List<String>) {
        Assertions.assertThat(retrievedAccounts.map { it.name }.containsAll(accountNames)).isTrue
    }

    @Then("the account with the name {string} has the following expenses:")
    fun assertAccountHasExpenses(accountName: String, expectedExpenses: List<Expense>) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(accountName)!!
        val actualExpenses = accountProjection.expenses

        Assertions.assertThat(actualExpenses).containsAll(expectedExpenses)
    }

    @Then("the account with the name {string} does not have the following expenses:")
    fun assertAccountDoesNotHaveExpenses(accountName: String, missingExpenses: List<Expense>) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(accountName)!!
        val actualExpenses = accountProjection.expenses

        Assertions.assertThat(actualExpenses).doesNotContainAnyElementsOf(missingExpenses)
    }

    @Suppress("unused")
    @DataTableType
    fun expenseTransformer(entry: Map<String, String>): Expense {
        return Expense(
            date = entry["date"]?.toUTCInstant() ?: Instant.now(),
            description = entry["description"] ?: "",
            amount = BigDecimal(entry["amount"])
        )
    }
}