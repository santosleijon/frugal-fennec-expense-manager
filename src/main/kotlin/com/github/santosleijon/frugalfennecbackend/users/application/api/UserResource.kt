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
import javax.validation.Valid
import javax.validation.constraints.*

@RestController
@RequestMapping("user")
class UserResource @Autowired constructor(
    private val startLoginCommand: StartLoginCommand,
    private val completeLoginCommand: CompleteLoginCommand,
    private val abortLoginCommand: AbortLoginCommand,
    private val logoutCommand: LogoutCommand,
    private val getCurrentUserSessionQuery: GetCurrentUserSessionQuery,
) {

    @PostMapping("start-login")
    fun startLogin(@Valid @RequestBody(required = true) startLoginInputsDTO: StartLoginInputsDTO) {
        val commandInput = StartLoginCommand.Input(startLoginInputsDTO.email)
        startLoginCommand.execute(commandInput)
    }

    data class StartLoginInputsDTO(
        @field:NotEmpty
        val email: String,
    )

    @PostMapping("complete-login")
    fun completeLogin(
        @Valid @RequestBody(required = true) completeLoginInputsDTO: CompleteLoginInputsDTO,
        response: HttpServletResponse?,
    ): CompleteLoginResultDTO {
        val commandInput = CompleteLoginCommand.Input(
            completeLoginInputsDTO.email,
            completeLoginInputsDTO.verificationCode,
        )

        val commandResult = completeLoginCommand.execute(commandInput)

        response?.addCookie(createSessionCookie(commandResult.userSession))

        return CompleteLoginResultDTO(commandResult.userId, commandResult.userEmail, commandResult.userSession)
    }

    data class CompleteLoginInputsDTO(
        @field:NotEmpty
        val email: String,

        @field:NotEmpty
        val verificationCode: String,
    )

    data class CompleteLoginResultDTO(
        val id: UUID,
        val email: String,
        val userSession: UserSession,
    )

    @PostMapping("abort-login")
    fun abortLogin(@Valid @RequestBody(required = true) abortLoginInputsDTO: AbortLoginInputsDTO) {
        val commandInput = AbortLoginCommand.Input(abortLoginInputsDTO.email)
        abortLoginCommand.execute(commandInput)
    }

    data class AbortLoginInputsDTO(
        @field:NotEmpty
        val email: String,
    )

    @PostMapping("logout")
    fun logout(
        @CookieValue(value = "sessionId") sessionId: UUID,
        response: HttpServletResponse?,
    ) {
        logoutCommand.execute(LogoutCommand.Input(sessionId))
        response?.addCookie(createDeletedSessionCookie())
    }

    @GetMapping("current-session")
    fun getCurrentUserSession(@CookieValue(value = "sessionId") sessionId: UUID?): GetCurrentUserSessionResultDTO {
        val queryInput = GetCurrentUserSessionQuery.Input(sessionId)
        return getCurrentUserSessionQuery.execute(queryInput)
    }

    data class GetCurrentUserSessionResultDTO(
        val hasValidUserSession: Boolean,
        val userId: UUID? = null,
        val email: String? = null,
        val userSession: UserSessionProjection? = null,
    )

    private fun createSessionCookie(userSession: UserSession): Cookie {
        val cookie = Cookie("sessionId", userSession.id.toString())
        
        val secondsUntilSessionExpiration = Duration.between(Instant.now(), userSession.validTo).seconds.toInt()
        
        cookie.maxAge = secondsUntilSessionExpiration
        cookie.path = "/"

        // TODO: Use secure http-only cookie
        cookie.secure = false

        return cookie
    }

    private fun createDeletedSessionCookie(): Cookie {
        val cookie = Cookie("sessionId", "")

        cookie.maxAge = 0
        cookie.path = "/"

        // TODO: Use secure http-only cookie
        cookie.secure = false

        return cookie
    }
}
