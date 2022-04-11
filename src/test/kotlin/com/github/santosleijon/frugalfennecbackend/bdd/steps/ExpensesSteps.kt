package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import com.github.santosleijon.frugalfennecbackend.bdd.utils.toDateString
import com.github.santosleijon.frugalfennecbackend.utils.toUTCInstant
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.Instant

class ExpensesSteps {

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountProjectionRepository: AccountProjectionRepository

    private val pageUrl = "http://localhost:8080"

    @Given("the account with the name {string} has the following expenses")
    fun givenExpenses(accountName: String, expenses: List<Expense>) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(accountName)
        val account = accountRepository.findByIdOrNull(accountProjection!!.id)!!

        expenses.forEach { expense ->
            account.addExpense(expense)
        }

        accountRepository.save(account)
    }

    @When("the user opens the expenses page")
    fun openTheAccountsPage() = runBlocking {
        AccountsSteps.webDriver.get(pageUrl)
        AccountsSteps.webDriver.clickOnButton("Expenses")
    }

    @When("the user selects account {string} in the add expense form")
    fun selectAccountInAddExpenseForm(accountName: String) {
        AccountsSteps.webDriver.selectOptionFromDropdownElement("account-field", accountName)
    }

    @When("the user enters expense date {string} in the add expense form")
    fun enterExpenseDateInAddExpenseForm(date: String) {
        val datePattern = "\\d{4}-\\d{2}-\\d{2}".toRegex()

        if (!datePattern.matches(date)) {
            error("Date must be specified in format YYYY-MM-DD")
        }

        val dateField = AccountsSteps.webDriver.findElement(
            By.xpath("//*[contains(@class, 'addExpenseForm')]//input[@type='date']")
        )

        dateField.sendKeys(
            date.substring(0, 4), Keys.ARROW_LEFT,
            date.substring(5, 7), Keys.ARROW_LEFT,
            date.substring(8, 10)
        )
    }

    @When("the user enters expense description {string} in the add expense form")
    fun enterExpenseDescriptionInAddExpenseForm(description: String) {
        AccountsSteps.webDriver.enterTextIntoElementWithId(description, "description-field")
    }

    @When("the user enters amount {bigdecimal} in the add expense form")
    fun enterAmountInAddExpenseForm(amount: BigDecimal) {
        AccountsSteps.webDriver.enterTextIntoElementWithId(amount.toString(), "amount-field")
    }

    @When("the user selects the following expenses from the expenses list")
    fun selectExpenseFromExpensesList(expenses: List<Expense>) {
        expenses.forEach { expense ->
            val xpath = """
                //div[@data-field='date'][text()='${expense.date.toDateString()}']/..
                //div[@data-field='description'][text()='${expense.description}']/..
                //div[@data-field='amount'][text()='${expense.amount}']/..
                //input[@aria-label='Select Row checkbox']
            """.trimIndent()

            val element = AccountsSteps.webDriver.findElement(By.xpath(xpath))

            element.click()
        }
    }

    @Then("the following expenses are displayed in the expenses list")
    fun assertExpensesAreDisplayedInExpensesList(expenses: List<Expense>) {
        val expensesDataGrid = AccountsSteps.webDriver.findElement(By.id("expensesDataGrid"))

        expenses.forEach {
            Assertions.assertThat(expensesDataGrid.text).contains(it.date.toDateString())
            Assertions.assertThat(expensesDataGrid.text).contains(it.description)
            Assertions.assertThat(expensesDataGrid.text).contains(it.amount.toString())
        }
    }

    @Then("the following expenses are not displayed in the expenses list")
    fun assertExpensesAreNotDisplayedInExpensesList(expenses: List<Expense>) {
        val expensesDataGrid = AccountsSteps.webDriver.findElement(By.id("expensesDataGrid"))

        expenses.forEach {
            Assertions.assertThat(expensesDataGrid.text).doesNotContain(it.date.toDateString())
            Assertions.assertThat(expensesDataGrid.text).doesNotContain(it.description)
            Assertions.assertThat(expensesDataGrid.text).doesNotContain(it.amount.toString())
        }
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
