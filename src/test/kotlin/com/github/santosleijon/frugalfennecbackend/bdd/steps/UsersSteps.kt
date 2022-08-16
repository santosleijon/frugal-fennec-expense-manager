package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockMailSender
import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockRandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.bdd.utils.waitFor
import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.domain.User
import com.github.santosleijon.frugalfennecbackend.users.domain.UserRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.RandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessions
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.MailSender
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

class UsersSteps {

    @Autowired
    private lateinit var userResource: UserResource

    @Autowired
    private lateinit var mailSender: MailSender

    @Autowired
    private lateinit var randomEmailVerificationCodeGenerator: RandomEmailVerificationCodeGenerator

    @Autowired
    private lateinit var userSessions: UserSessions

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userProjectionRepository: UserProjectionRepository

    @Autowired
    private lateinit var userSessionProjectionRepsitory: UserSessionProjectionRepository

    var userSession: UserSession? = null
    var sessionUserId: UUID = userSession?.userId ?: UUID.randomUUID()

    private val loginPageUrl = "http://localhost:8080"

    @Before
    fun before() {
        MockMailSender.init(mailSender)
        MockRandomEmailVerificationCodeGenerator.init(randomEmailVerificationCodeGenerator)

        userSession = null
    }

    @Given("that the randomly generated email verification code will be {string}")
    fun givenRandomlyGeneratedEmailVerificationCode(verificationCode: String) {
        MockRandomEmailVerificationCodeGenerator.setVerificationCode(verificationCode)
    }

    @Given("a registered user {string}")
    fun givenARegisteredUser(email: String) {
        if (userProjectionRepository.findByEmail(email) != null) {
            return
        }

        val user = User(
            id = UUID.randomUUID(),
            email = email,
        )

        userRepository.save(user)
    }

    @When("the user opens the login page")
    fun theUserOpensTheLoginPage() {
        AccountsSteps.webDriver.get(loginPageUrl)
    }

    @When("the user enters email {string}")
    fun enterAccountName(email: String) = runBlocking {
        AccountsSteps.webDriver.enterTextIntoElementWithId(email, "email-field")
    }

    @When("the user enters email verification code {string}")
    fun enterEmailVerificationCode(verificationCode: String) = runBlocking {
        AccountsSteps.webDriver.enterTextIntoElementWithId(verificationCode, "verification-code-field")
    }

    @When("a user with email {string} completes a login with verification code {string}")
    fun userCompletedLogin(email: String, verificationCode: String) {
        val completeLoginInputsDTO = UserResource.CompleteLoginInputsDTO(
            email,
            verificationCode,
        )

        userSession = userResource.completeLogin(completeLoginInputsDTO, null)
    }

    @Then("an email with verification code {string} is sent to {string}")
    fun assertAnEmailWithVerificationCodeHasBeenSentTo(verificationCode: String, email: String) {
        val sentEmail = MockMailSender.sentEmails.first()

        val actualRecipientEmail = sentEmail.personalization.first {
            it.tos.isNotEmpty()
        }
            .tos
            .first()
            .email

        val actualVerificationCode = sentEmail.personalization.first {
            it.dynamicTemplateData.isNotEmpty()
        }
            .dynamicTemplateData["verificationCode"]

        Assertions.assertThat(actualRecipientEmail).isEqualTo(email)
        Assertions.assertThat(actualVerificationCode).isEqualTo(verificationCode)
    }

    @Then("a user is created for email {string}")
    fun assertUserIsCreatedFor(email: String) {
        val user = userProjectionRepository.findByEmail(email)

        Assertions.assertThat(user).isNotNull
    }

    @Then("a user session is created for user with email {string}")
    fun assertUserSessionIsCreatedForUserWith(email: String) {
        val user = userProjectionRepository.findByEmail(email)

        val userSession = userSessionProjectionRepsitory.findByUserId(user!!.id)

        Assertions.assertThat(userSession).isNotEmpty
    }

    @Then("the user sees the complete login form")
    fun assertUserSeesTheCompleteLoginForm() {
        val pageContent = AccountsSteps.webDriver.getPageContent()

        Assertions.assertThat(pageContent).contains("Complete login by entering verification code")
    }

    @Then("the user is redirected to the reports page")
    fun assertTheUserIsRedirectedToTheReportsPage() = runBlocking {
        waitFor(1000)

        val currentUrl = AccountsSteps.webDriver.currentUrl

        Assertions.assertThat(currentUrl).contains("reports")
    }

    @Then("the user sees the error message {string}")
    fun assertTheUserSeesError(message: String) = runBlocking {
        val pageContent = AccountsSteps.webDriver.getPageContent()

        Assertions.assertThat(pageContent).contains(message)
    }
}
