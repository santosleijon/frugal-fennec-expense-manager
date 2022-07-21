package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockMailSender
import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockRandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.domain.RandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.users.domain.SessionTokens
import com.github.santosleijon.frugalfennecbackend.users.infrastructure.MailSender
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

class UsersSteps {

    @Autowired
    private lateinit var userResource: UserResource

    @Autowired
    private lateinit var mailSender: MailSender

    @Autowired
    private lateinit var randomEmailVerificationCodeGenerator: RandomEmailVerificationCodeGenerator

    @Autowired
    private lateinit var sessionTokens: SessionTokens

    private var sessionToken: String? = null
    private var requestError: Exception? = null

    @Before
    fun before() {
        MockMailSender.init(mailSender)
        MockRandomEmailVerificationCodeGenerator.init(randomEmailVerificationCodeGenerator)

        sessionToken = null
        requestError = null
    }

    @Given("that the randomly generated email verification code will be {string}")
    fun givenRandomlyGeneratedEmailVerificationCode(verificationCode: String) {
        MockRandomEmailVerificationCodeGenerator.setVerificationCode(verificationCode)
    }

    @When("a user with email {string} starts logging in")
    fun userWithEmailStartsLoggingIn(email: String) {
        userResource.startLogin(
            UserResource.StartLoginInputsDTO(email)
        )
    }

    @When("a user with email {string} completes the login with verification code {string}")
    fun userCompletedLogin(email: String, verificationCode: String) {
        val completeLoginInputsDTO = UserResource.CompleteLoginInputsDTO(
            email,
            verificationCode,
        )

        try {
            sessionToken = userResource.completeLogin(completeLoginInputsDTO).sessionToken
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

    @Then("the user receives a valid session token")
    fun assertUserReceivesAValidSessionToken() {
        Assertions.assertThat(sessionToken).isNotNull

        sessionTokens.verifyToken(sessionToken!!)
    }

    @Then("an {string} error is returned")
    fun assertAnErrorIsReturned(errorName: String) {
        val actualErrorName = requestError!!::class.java.simpleName

        Assertions.assertThat(actualErrorName).isEqualTo(errorName)
    }
}
