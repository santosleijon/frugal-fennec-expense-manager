package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.accounts.application.api.AccountResource
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.bdd.TestDbContainer
import com.github.santosleijon.frugalfennecbackend.bdd.utils.waitFor
import com.github.santosleijon.frugalfennecbackend.bdd.webdriver.ExtendedWebDriver
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import java.util.*

class AccountsSteps {

    // TODO: Move WebDriver stuff to CommonSteps class
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

        lateinit var webDriver: ExtendedWebDriver
    }

    private val pageUrl = "http://localhost:8080"

    @Autowired
    private lateinit var accountResource: AccountResource

    @Autowired
    private lateinit var accountProjectionRepository: AccountProjectionRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var userProjectionRepository: UserProjectionRepository

    @Autowired
    private lateinit var usersSteps: UsersSteps

    @Autowired
    private lateinit var commonSteps: CommonSteps

    @Before
    fun beforeScenario() {
        webDriver = ExtendedWebDriver.createWithWebDriverManager()
    }

    @After
    fun afterScenario() {
        webDriver.quit()
        deleteAllAccounts()
    }

    @Given("user {string} has an account with the name {string}")
    fun givenAnAccount(userEmail: String, accountName: String) {
        val userId = userProjectionRepository.findByEmail(userEmail)!!.id

        val account = Account(UUID.randomUUID(), accountName, userId)

        accountRepository.save(account)
    }

    @When("the user clicks on {string}")
    fun click(buttonText: String) = runBlocking {
        webDriver.clickOnButton(buttonText)
    }

    @When("the user enters new account name {string} into the account name cell for {string}")
    fun enterNewAccountName(newAccountName: String, oldAccountName: String) = runBlocking {
        webDriver.doubleClickOnElementWithText(oldAccountName)
        val accountNameInputElement = webDriver.findInputElementByValue(oldAccountName)
        webDriver.enterValueIntoInputElement(accountNameInputElement, newAccountName)
        accountNameInputElement.sendKeys(Keys.ENTER)
        waitFor(500L)
    }

    @When("the user opens the accounts page")
    fun openTheAccountsPage() = runBlocking {
        webDriver.get(pageUrl)
        webDriver.clickOnButton("Accounts")
    }

    @When("the user selects {string} in the accounts list")
    fun selectAccountInAccountsList(accountName: String) {
        val checkbox = webDriver.findElement(By.xpath("//*[contains(text(),'$accountName')]/..//input[@type='checkbox']"))
        checkbox.click()
    }

    @When("the user enters account name {string}")
    fun enterAccountName(accountName: String) = runBlocking {
        webDriver.enterTextIntoElementWithId(accountName, "name-field")
    }

    @When("accounts are retrieved without a valid user session cookie")
    fun retrieveAccountsWithoutAValidUserSessionCookie() {
        try {
            accountResource.getAll(commonSteps.invalidUserSessionToken)
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @When("an account is created without a valid user session cookie")
    fun createAnAccountWithoutAValidUserSessionCookie() {
        try {
            accountResource.create(
                createAccountInputsDTO = AccountResource.CreateAccountInputsDTO("test-account"),
                sessionToken = commonSteps.invalidUserSessionToken,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @When("an account is renamed without a valid user session cookie")
    fun renameAnAccountWithoutAValidUserSessionCookie() {
        try {
            accountResource.updateName(
                id = UUID.randomUUID(),
                updateAccountNameInputsDTO = AccountResource.UpdateAccountNameInputsDTO("new-test-account-name"),
                sessionToken = commonSteps.invalidUserSessionToken,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @When("an account is deleted without a valid user session cookie")
    fun deleteAnAccountWithoutAValidUserSessionCookie() {
        try {
            accountResource.delete(
                id = UUID.randomUUID(),
                sessionToken = commonSteps.invalidUserSessionToken,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @When("user {string} tries to rename the account {string} to {string}")
    fun userTriesToRenameAccount(userEmail: String, originalAccountName: String, newAccountName: String) {
        usersSteps.givenUserHasLoggedIn(userEmail)

        val accountId = accountProjectionRepository.findByNameOrNull(originalAccountName)!!.id

        try {
            accountResource.updateName(
                id = accountId,
                updateAccountNameInputsDTO = AccountResource.UpdateAccountNameInputsDTO(newAccountName),
                sessionToken = usersSteps.userSession!!.token!!,
            )
        } catch (e: Exception) {
            commonSteps.requestException = e
        }
    }

    @Then("the account {string} is displayed in the accounts list")
    fun assertAccountIsDisplayedInList(accountName: String) {
        val accountsDataGrid = webDriver.findElement(By.id("accountsDataGrid"))
        Assertions.assertThat(accountsDataGrid.text).contains(accountName)
    }

    @Then("the account {string} is not displayed in the accounts list")
    fun assertAccountIsNotDisplayedInList(accountName: String) {
        val accountsDataGrid = webDriver.findElement(By.id("accountsDataGrid"))
        Assertions.assertThat(accountsDataGrid.text).doesNotContain(accountName)
    }

    @Then("an account with the name {string} exists")
    fun assertAccountExists(accountName: String) {
        val account = accountProjectionRepository.findByNameAndUserIdOrNull(accountName, usersSteps.sessionUserId!!)
        Assertions.assertThat(account).isNotNull
    }

    @Then("the account with the name {string} has the following expenses:")
    fun assertAccountHasExpenses(accountName: String, expectedExpenses: List<Expense>) {
        val accountProjection = accountProjectionRepository.findByNameAndUserIdOrNull(accountName, usersSteps.sessionUserId!!)!!
        val actualExpenses = accountProjection.expenses

        Assertions.assertThat(actualExpenses).containsAll(expectedExpenses)
    }

    fun deleteAllAccounts() {
        val sessionToken = usersSteps.userSession?.token

        if (sessionToken != null) {
            accountResource.getAll(sessionToken).forEach { accountProjection ->
                accountResource.delete(accountProjection.id, sessionToken)
            }
        }
    }
}
