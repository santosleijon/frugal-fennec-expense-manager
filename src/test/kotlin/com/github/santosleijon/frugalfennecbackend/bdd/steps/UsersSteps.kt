package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockMailSender
import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockRandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.bdd.utils.waitUntilOrThrow
import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.application.commands.CompleteLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.domain.User
import com.github.santosleijon.frugalfennecbackend.users.domain.UserRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.RandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.MailSender
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCodeRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCode
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletResponse
import java.util.*
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.openqa.selenium.Cookie

class UsersSteps {

    @Autowired
    private lateinit var userResource: UserResource

    @Autowired
    private lateinit var mailSender: MailSender

    @Autowired
    private lateinit var randomEmailVerificationCodeGenerator: RandomEmailVerificationCodeGenerator

    @Autowired
    private lateinit var completeLoginCommand: CompleteLoginCommand

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userProjectionRepository: UserProjectionRepository

    @Autowired
    private lateinit var userSessionProjectionRepsitory: UserSessionProjectionRepository

    @Autowired
    private lateinit var emailVerificationCodeRepository: EmailVerificationCodeRepository

    @Autowired
    private lateinit var accountProjectionRepository: AccountProjectionRepository

    var userSession: UserSession? = null
    var sessionUserId: UUID? = userSession?.userId

    private val loginPageUrl = "http://localhost:8080"

    @Before
    fun beforeScenario() {
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
        val existingUser = userProjectionRepository.findByEmail(email)

        if (existingUser != null) {
            sessionUserId = existingUser.id
            return
        }

        val user = User(
            id = UUID.randomUUID(),
            email = email,
        )

        sessionUserId = user.id

        userRepository.save(user)
    }

    @Given("{int} logins have been started")
    fun givenANumberOfLoginsHaveBeenStarted(startedLoginsCount: Int) {
        repeat(startedLoginsCount) { index ->
            val emailVerificationCode = EmailVerificationCode(
                email = "test@example.com",
                verificationCode = (1000 + index).toString(),
                issued = Instant.now(),
                validTo = Instant.now().plus(Duration.ofMinutes(1)),
            )

            emailVerificationCodeRepository.save(emailVerificationCode)
        }
    }

    @Given("the user with email {string} has logged in")
    fun givenUserHasLoggedIn(email: String) = runBlocking {
        givenARegisteredUser(email)

        val userId = userProjectionRepository.findByEmail(email)!!.id

        val newUserSession = completeLoginCommand.createUserSession(userId)

        userSession = newUserSession

        val cookie = Cookie.Builder("sessionId", newUserSession.id.toString())
            .path("/")
            .isSecure(false)
            .build()

        CommonSteps.webDriver.get(loginPageUrl)
        CommonSteps.webDriver.manage().addCookie(cookie)
        CommonSteps.webDriver.navigate().refresh()
    }

    @When("the user opens the login page")
    fun theUserOpensTheLoginPage() {
        CommonSteps.webDriver.get(loginPageUrl)
    }

    @When("the user enters email {string}")
    fun enterEmail(email: String) = runBlocking {
        CommonSteps.webDriver.enterTextIntoElementWithId(email, "email-field")
    }

    @When("the user enters email verification code {string}")
    fun enterEmailVerificationCode(verificationCode: String) = runBlocking {
        CommonSteps.webDriver.enterTextIntoElementWithId(verificationCode, "verification-code-field")
    }

    @When("a user with email {string} completes a login with verification code {string}")
    fun userCompletedLogin(email: String, verificationCode: String) {
        val completeLoginInputsDTO = UserResource.CompleteLoginInputsDTO(
            email,
            verificationCode,
        )

        val completeLoginResult = userResource.completeLogin(completeLoginInputsDTO, MockHttpServletResponse())

        userSession = completeLoginResult.userSession
    }

    @When("the user refreshes the page")
    fun theUserRefreshesThePage() {
        CommonSteps.webDriver.navigate().refresh()
    }

    @Then("an email with verification code {string} is sent to {string}")
    fun assertAnEmailWithVerificationCodeHasBeenSentTo(verificationCode: String, email: String) = runBlocking {
        waitUntilOrThrow {
            if (MockMailSender.sentEmails.size < 1) {
                false
            } else {
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

                actualRecipientEmail == email && actualVerificationCode == verificationCode
            }
        }
    }

    @Then("a user is created for email {string}")
    fun assertUserIsCreatedFor(email: String): Unit = runBlocking {
        waitUntilOrThrow {
            userProjectionRepository.findByEmail(email) != null
        }
    }

    @Then("a user session is created for user with email {string}")
    fun assertUserSessionIsCreatedForUserWith(email: String): Unit = runBlocking {
        val user = userProjectionRepository.findByEmail(email)

        waitUntilOrThrow {
            userSessionProjectionRepsitory.findByUserId(user!!.id).isNotEmpty()
        }
    }

    @Then("the user sees the complete login form")
    fun assertUserSeesTheCompleteLoginForm() {
        val pageContent = CommonSteps.webDriver.getPageContent()

        Assertions.assertThat(pageContent).contains("Complete login by entering verification code")
    }

    @Then("the user is redirected to the start page")
    fun assertTheUserIsRedirectedToTheStartPage(): Any = runBlocking {
        waitUntilOrThrow {
            CommonSteps.webDriver.currentUrl == "http://localhost:8080/"
        }
    }

    @Then("the user sees the error message {string}")
    fun assertTheUserSeesError(message: String): Unit = runBlocking {
        waitUntilOrThrow {
            CommonSteps.webDriver.getPageContent().contains(message)
        }
    }

    @Then("the user is redirected back the start login form")
    fun assertTheUserIsRedirectedBackToStartLoginForm(): Unit = runBlocking {
        waitUntilOrThrow {
            CommonSteps.webDriver.getPageContent().contains("Enter your email to login or register")
        }
    }

    @Then("no unconsumed email verification codes exist for user with email {string}")
    fun assertTheUserIsRedirectedBackToStartLoginForm(email: String) {
        val unconsumedVerificationCodesCount = emailVerificationCodeRepository.countUnconsumedByEmail(email)

        Assertions.assertThat(unconsumedVerificationCodesCount).isEqualTo(0)
    }

    @Then("the user session is ended for user with email {string}")
    fun assertTheUserSessionIsEnded(email: String) {
        val user = userProjectionRepository.findByEmail(email)!!
        val userSession = userSessionProjectionRepsitory.findByUserId(user.id).last()
        
        val sessionHasEnded = Duration.between(userSession.validTo, Instant.now()).isNegative

        Assertions.assertThat(sessionHasEnded).isTrue
    }

    @Then("the user is still logged in")
    fun assertUserIsStillLoggedIn() {
        Assertions.assertThat(CommonSteps.webDriver.getPageContent().lowercase()).contains("logout")
    }

    @Then("the user sees the confirm delete user dialog")
    fun assertUserSeesConfirmDeleteUserDialog() {
        Assertions.assertThat(CommonSteps.webDriver.getPageContent()).contains("Are you sure you want to permanently delete your user account")
    }

    @Then("the user data is deleted")
    fun assertUserDataIsDeleted(): Unit = runBlocking {
        waitUntilOrThrow {
            userRepository.findById(sessionUserId!!) == null &&
            userProjectionRepository.findById(sessionUserId!!) == null &&
            accountProjectionRepository.findByUserId(sessionUserId!!).isEmpty() &&
            userSessionProjectionRepsitory.findByUserId(sessionUserId!!).isEmpty()
        }
    }
}
