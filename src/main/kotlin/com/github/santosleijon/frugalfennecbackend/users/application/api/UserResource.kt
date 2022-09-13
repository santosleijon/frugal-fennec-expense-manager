package com.github.santosleijon.frugalfennecbackend.users.application.api

import com.github.santosleijon.frugalfennecbackend.users.application.commands.CompleteLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.StartLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.AbortLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.LogoutCommand
import com.github.santosleijon.frugalfennecbackend.users.domain.UserAuthorizer
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseCookie
import java.time.Duration
import java.time.Instant
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import java.util.UUID

@RestController
@RequestMapping("user")
class UserResource @Autowired constructor(
    private val startLoginCommand: StartLoginCommand,
    private val completeLoginCommand: CompleteLoginCommand,
    private val abortLoginCommand: AbortLoginCommand,
    private val logoutCommand: LogoutCommand,
    private val userAuthorizer: UserAuthorizer,
) {

    companion object {
        private val SESSION_TOKEN_COOKIE_NAME = "sessionToken"
    }

    @PostMapping("start-login")
    fun startLogin(@RequestBody(required = true) startLoginInputsDTO: StartLoginInputsDTO) {
        startLoginCommand.handle(startLoginInputsDTO.email)
    }

    @PostMapping("complete-login")
    fun completeLogin(
        @RequestBody(required = true) completeLoginInputsDTO: CompleteLoginInputsDTO,
        response: HttpServletResponse?,
    ): CompleteLoginResultDTO {
        val result = completeLoginCommand.handle(completeLoginInputsDTO.email, completeLoginInputsDTO.verificationCode)

        response?.addCookie(createSessionTokenCookie(result.userSession))

        return CompleteLoginResultDTO(result.userId, result.userEmail, result.userSession)
    }

    @PostMapping("abort-login")
    fun abortLogin(@RequestBody(required = true) abortLoginInputsDTO: AbortLoginInputsDTO) {
        abortLoginCommand.handle(abortLoginInputsDTO.email)
    }

    @PostMapping("logout")
    fun logout(
        @CookieValue(value = "sessionToken") sessionToken: String,
        response: HttpServletResponse?,
    ) {
        logoutCommand.handle(sessionToken)
        
        response?.addCookie(createDeletedSessionTokenCookie())
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
