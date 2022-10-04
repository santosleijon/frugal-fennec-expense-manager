package com.github.santosleijon.frugalfennecbackend.bdd.steps

import com.github.santosleijon.frugalfennecbackend.bdd.TestDbContainer
import com.github.santosleijon.frugalfennecbackend.bdd.webdriver.ExtendedWebDriver
import com.github.santosleijon.frugalfennecbackend.common.errors.UnauthorizedOperation
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidSessionToken
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

class CommonSteps {
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

    val invalidUserSessionToken = "invalid-user-session-token"

    var requestException: Exception? = null

    @Before
    fun beforeScenario() {
        webDriver = ExtendedWebDriver.createWithWebDriverManager()
    }

    @After
    fun afterScenario() {
        webDriver.quit()
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