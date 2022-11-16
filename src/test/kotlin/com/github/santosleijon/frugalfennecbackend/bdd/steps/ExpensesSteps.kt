package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.accounts.application.api.AccountResource
import com.github.santosleijon.frugalfennecbackend.accounts.application.api.utils.toUTCInstant
import com.github.santosleijon.frugalfennecbackend.bdd.utils.toDateString
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import io.cucumber.java.After
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

    @Autowired
    private lateinit var accountResource: AccountResource

    @Autowired
    private lateinit var usersSteps: UsersSteps

    @Autowired
    private lateinit var commonSteps: CommonSteps

    @Autowired
    private lateinit var accountSteps: AccountsSteps

    private val pageUrl = "http://localhost:8080"

    @After
    fun afterScenario() {
        accountSteps.deleteAllAccounts()
    }

    @Given("the account with the name {string} has the following expenses")
    fun givenExpenses(accountName: String, expenses: List<Expense>) {
        val accountProjection = accountProjectionRepository.findByNameOrNull(accountName)!!
        val account = accountRepository.findByIdOrNull(accountProjection.id)!!

        expenses.forEach { expense ->
            account.addExpense(expense, usersSteps.sessionUserId!!)
        }

        accountRepository.save(account)
    }

    @When("the user opens the expenses page")
    fun openTheAccountsPage() = runBlocking {
        CommonSteps.webDriver.get(pageUrl)
        CommonSteps.webDriver.clickOnButton("Expenses")
    }

    @When("the user selects account {string} in the add expense form")
    fun selectAccountInAddExpenseForm(accountName: String) {
        CommonSteps.webDriver.selectOptionFromDropdownElement("account-field", accountName)
    }

    @When("the user enters expense date {string} in the add expense form")
    fun enterExpenseDateInAddExpenseForm(date: String) {
        val datePattern = "\\d{4}-\\d{2}-\\d{2}".toRegex()

        if (!datePattern.matches(date)) {
            error("Date must be specified in format YYYY-MM-DD")
        }

        val dateField = CommonSteps.webDriver.findElement(
            By.xpath("//*[contains(@class, 'addExpenseForm')]//input[@type='date']")
        )

        dateField.sendKeys(
            date.substring(0, 4), Keys.ARROW_RIGHT,
            date.substring(5, 7), Keys.ARROW_RIGHT,
            date.substring(8, 10)
        )
    }

    @When("the user enters expense description {string} in the add expense form")
    fun enterExpenseDescriptionInAddExpenseForm(description: String) = runBlocking {
        CommonSteps.webDriver.enterTextIntoElementWithId(description, "description-field")
    }

    @When("the user enters amount {bigdecimal} in the add expense form")
    fun enterAmountInAddExpenseForm(amount: BigDecimal) = runBlocking {
        CommonSteps.webDriver.enterTextIntoElementWithId(amount.toString(), "amount-field")
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

            val element = CommonSteps.webDriver.findElement(By.xpath(xpath))

            element.click()
        }
    }

    @When("an expense is added to account {string} by a user without a valid user session cookie")
    fun addExpenseToAccountWithoutAValidUserSessionCookie(accountName: String) {
        val account = accountProjectionRepository.findByNameAndUserIdOrNull(accountName, usersSteps.sessionUserId!!)!!

        try {
            accountResource.addExpense(
                account.id,
                AccountResource.ExpenseInputsDTO(
                    "2022-01-01",
                    "Expense description",
                    BigDecimal.ONE,
                ),
                sessionId = commonSteps.invalidUserSessionId,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @When("the expense on account {string} is deleted by a user without a valid user session cookie")
    fun expenseOnAccountIsDeletedWithoutAValidUserSessionCookie(accountName: String) {
        val account = accountProjectionRepository.findByNameAndUserIdOrNull(accountName, usersSteps.sessionUserId!!)!!

        val expenseToDelete = account.expenses.first()

        try {
            accountResource.deleteExpense(
                account.id,
                AccountResource.ExpenseInputsDTO(
                    expenseToDelete.date.toDateString(),
                    expenseToDelete.description,
                    expenseToDelete.amount,
                ),
                sessionId = commonSteps.invalidUserSessionId,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @When("user {string} tries to add an expense to the account {string}")
    fun userTriesToAddExpenseToAccount(userEmail: String, accountName: String) {
        usersSteps.givenUserHasLoggedIn(userEmail)

        val accountId = accountProjectionRepository.findByNameOrNull(accountName)!!.id

        try {
            accountResource.addExpense(
                id = accountId,
                expenseInputsDTO = AccountResource.ExpenseInputsDTO(
                    date = Instant.now().toDateString(),
                    description = "Expense description",
                    amount = BigDecimal.ONE,
                ),
                sessionId = usersSteps.userSession!!.id,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @When("user {string} tries to delete the expense on account {string}")
    fun userTriesToDeleteExpenseOnAccount(userEmail: String, accountName: String) {
        usersSteps.givenUserHasLoggedIn(userEmail)

        val account = accountProjectionRepository.findByNameOrNull(accountName)!!

        val expense = account.expenses.first()

        try {
            accountResource.deleteExpense(
                id = account.id,
                expenseInputsDTO = AccountResource.ExpenseInputsDTO(
                    date = expense.date.toDateString(),
                    description = expense.description,
                    amount = expense.amount,
                ),
                sessionId = usersSteps.userSession!!.id,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @Then("the following expenses are displayed in the expenses list")
    fun assertExpensesAreDisplayedInExpensesList(expenses: List<Expense>) {
        val expensesDataGrid = CommonSteps.webDriver.findElement(By.id("expensesDataGrid"))

        expenses.forEach {
            Assertions.assertThat(expensesDataGrid.text).contains(it.date.toDateString())
            Assertions.assertThat(expensesDataGrid.text).contains(it.description)
            Assertions.assertThat(expensesDataGrid.text).contains(it.amount.toString())
        }
    }

    @Then("the following expenses are not displayed in the expenses list")
    fun assertExpensesAreNotDisplayedInExpensesList(expenses: List<Expense>) {
        val expensesDataGrid = CommonSteps.webDriver.findElement(By.id("expensesDataGrid"))

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
