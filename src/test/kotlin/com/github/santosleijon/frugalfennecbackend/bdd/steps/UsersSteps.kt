package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockMailSender
import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockRandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.domain.User
import com.github.santosleijon.frugalfennecbackend.users.domain.UserRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.RandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessions
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.MailSender
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

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

    var userSession: UserSession? = null
    var sessionUserId: UUID = userSession?.userId ?: UUID.randomUUID()

    private var requestError: Exception? = null

    @Before
    fun before() {
        MockMailSender.init(mailSender)
        MockRandomEmailVerificationCodeGenerator.init(randomEmailVerificationCodeGenerator)

        userSession = null
        requestError = null
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

    @When("a user with email {string} starts logging in")
    fun userWithEmailStartsLoggingIn(email: String) {
        try {
            userResource.startLogin(
                UserResource.StartLoginInputsDTO(email)
            )
        } catch (e: Exception) {
            requestError = e
        }
    }

    @When("a user with email {string} completes a login with verification code {string}")
    fun userCompletedLogin(email: String, verificationCode: String) {
        val completeLoginInputsDTO = UserResource.CompleteLoginInputsDTO(
            email,
            verificationCode,
        )

        try {
            userSession = userResource.completeLogin(completeLoginInputsDTO, null)
        } catch (e: Exception) {
            requestError = e
        }
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

    @Then("the user receives a valid session token")
    fun assertUserReceivesAValidSessionToken() {
        Assertions.assertThat(userSession).isNotNull
        Assertions.assertThat(userSession?.token).isNotNull
        Assertions.assertThat(userSessions.isValid(userSession!!.token!!)).isTrue
    }

    @Then("an {string} error is returned")
    fun assertAnErrorIsReturned(errorName: String) {
        val actualErrorName = requestError!!::class.java.simpleName

        Assertions.assertThat(actualErrorName).isEqualTo(errorName)
    }
}
