package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.bdd.mocks.MockMailSender
import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.infrastructure.MailSender
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

class UsersSteps {

    @Autowired
    private lateinit var userResource: UserResource

    @Autowired
    private lateinit var mailSender: MailSender

    @Before
    fun before() {
        MockMailSender.init(mailSender)
    }

    @When("a user with email {string} starts logging in")
    fun userWithEmailStartsLoggingIn(email: String) {
        userResource.startLogin(
            UserResource.StartLoginInputsDTO(email)
        )
    }

    @Then("an email with a verification code is sent to {string}")
    fun assertAnEmailWithVerificationCodeHasBeenSentTo(email: String) {
        val anySentEmailWithVerificationCode = MockMailSender.sentEmails.any { mail ->
            mail.personalization.any { personalization ->
                personalization.tos.any { to -> to.email == email }
            }
        } && MockMailSender.sentEmails.any { mail ->
            mail.personalization.any { personalization ->
                personalization.dynamicTemplateData.isNotEmpty()
            }
        }

        Assertions.assertThat(anySentEmailWithVerificationCode).isTrue
    }
}