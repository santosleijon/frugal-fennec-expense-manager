package com.github.santosleijon.frugalfennecbackend.users.application.api

import com.github.santosleijon.frugalfennecbackend.users.application.commands.AbortLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.CompleteLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.LogoutCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.StartLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.queries.GetCurrentUserSessionQuery
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("user")
class UserResource @Autowired constructor(
    private val startLoginCommand: StartLoginCommand,
    private val completeLoginCommand: CompleteLoginCommand,
    private val abortLoginCommand: AbortLoginCommand,
    private val logoutCommand: LogoutCommand,
    private val getCurrentUserSessionQuery: GetCurrentUserSessionQuery,
) {

    companion object {
        private const val SESSION_TOKEN_COOKIE_NAME = "sessionToken"
    }

    @PostMapping("start-login")
    fun startLogin(@RequestBody(required = true) startLoginInputsDTO: StartLoginInputsDTO) {
        val commandInput = StartLoginCommand.Input(startLoginInputsDTO.email)
        startLoginCommand.execute(commandInput)
    }

    @PostMapping("complete-login")
    fun completeLogin(
        @RequestBody(required = true) completeLoginInputsDTO: CompleteLoginInputsDTO,
        response: HttpServletResponse?,
    ): CompleteLoginResultDTO {
        val commandInput = CompleteLoginCommand.Input(
            completeLoginInputsDTO.email,
            completeLoginInputsDTO.verificationCode,
        )

        val commandResult = completeLoginCommand.execute(commandInput)

        response?.addCookie(createSessionTokenCookie(commandResult.userSession))

        return CompleteLoginResultDTO(commandResult.userId, commandResult.userEmail, commandResult.userSession)
    }

    @PostMapping("abort-login")
    fun abortLogin(@RequestBody(required = true) abortLoginInputsDTO: AbortLoginInputsDTO) {
        val commandInput = AbortLoginCommand.Input(abortLoginInputsDTO.email)
        abortLoginCommand.execute(commandInput)
    }

    @PostMapping("logout")
    fun logout(
        @CookieValue(value = "sessionToken") sessionToken: String,
        response: HttpServletResponse?,
    ) {
        logoutCommand.execute(LogoutCommand.Input(sessionToken))
        response?.addCookie(createDeletedSessionTokenCookie())
    }

    @GetMapping("current-session")
    fun getCurrentUserSession(@CookieValue(value = "sessionToken") sessionToken: String?): GetCurrentUserSessionDTO {
        return getCurrentUserSessionQuery.handle(sessionToken)
    }

    data class StartLoginInputsDTO(
        val email: String,
    )

    data class CompleteLoginInputsDTO(
        val email: String,
        val verificationCode: String,
    )

    data class CompleteLoginResultDTO(
        val id: UUID,
        val email: String,
        val userSession: UserSession,
    )

    data class AbortLoginInputsDTO(
        val email: String,
    )

    data class GetCurrentUserSessionDTO(
        val hasValidUserSession: Boolean,
        val userId: UUID? = null,
        val email: String? = null,
        val userSession: UserSessionProjection? = null,
    )

    private fun createSessionTokenCookie(userSession: UserSession): Cookie {
        val cookie = Cookie("sessionToken", userSession.token)
        
        val secondsUntilSessionExpiration = Duration.between(Instant.now(), userSession.validTo).seconds.toInt()
        
        cookie.maxAge = secondsUntilSessionExpiration
        cookie.path = "/"

        // TODO: Use secure http-only cookie
        cookie.secure = false

        return cookie
    }

    private fun createDeletedSessionTokenCookie(): Cookie {
        val cookie = Cookie(SESSION_TOKEN_COOKIE_NAME, "")

        cookie.maxAge = 0
        cookie.path = "/"

        // TODO: Use secure http-only cookie
        cookie.secure = false

        return cookie
    }
}
