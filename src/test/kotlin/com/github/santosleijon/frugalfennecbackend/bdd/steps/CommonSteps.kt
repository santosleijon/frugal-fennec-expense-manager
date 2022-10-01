package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidSessionToken
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions

class CommonSteps {
    val invalidUserSessionToken = "invalid-user-session-token"

    var requestException: Exception? = null

    @Before
    fun beforeScenario() {
        requestException = null
    }

    @Then("an InvalidSessionToken error is returned")
    fun assertErrorIsReturned() {
        Assertions.assertThat(requestException).isNotNull
        Assertions.assertThat(requestException).isInstanceOf(InvalidSessionToken::class.java)
    }
}
