package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.common.errors.UnauthorizedOperation
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidSessionToken
import io.cucumber.java.After
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions

class CommonSteps {
    val invalidUserSessionToken = "invalid-user-session-token"

    var requestException: Exception? = null

     @After
     fun afterScenario() {
         requestException = null
     }

    @Then("an InvalidSessionToken error is returned")
    fun assertInvalidSessionTokenErrorIsReturned() {
        Assertions.assertThat(requestException).isNotNull
        Assertions.assertThat(requestException).isInstanceOf(InvalidSessionToken::class.java)
    }

    @Then("an UnauthorizedOperation error is returned")
    fun assertUnauthorizedOperationErrorIsReturned() {
        Assertions.assertThat(requestException).isNotNull
        Assertions.assertThat(requestException).isInstanceOf(UnauthorizedOperation::class.java)
    }
}
