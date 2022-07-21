package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockMailSender
import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockRandomEmailVerificationCodeGenerator
import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.domain.RandomEmailVerificationCodeGenerator
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

    @Before
    fun before() {
        MockMailSender.init(mailSender)
        MockRandomEmailVerificationCodeGenerator.init(randomEmailVerificationCodeGenerator)
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
}
