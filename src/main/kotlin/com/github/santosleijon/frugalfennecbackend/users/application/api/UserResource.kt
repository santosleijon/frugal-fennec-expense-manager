package com.github.santosleijon.frugalfennecbackend.users.application.api

import com.github.santosleijon.frugalfennecbackend.users.application.commands.CompleteLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.StartLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.time.Instant
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("user")
class UserResource @Autowired constructor(
    private val startLoginCommand: StartLoginCommand,
    private val completeLoginCommand: CompleteLoginCommand,
) {
    @PostMapping("start-login")
    fun startLogin(@RequestBody(required = true) startLoginInputsDTO: StartLoginInputsDTO) {
        startLoginCommand.handle(startLoginInputsDTO.userEmail)
    }

    @PostMapping("complete-login")
    fun completeLogin(
        @RequestBody(required = true) completeLoginInputsDTO: CompleteLoginInputsDTO,
        response: HttpServletResponse?,
    ): UserSession {
        val userSession = completeLoginCommand.handle(completeLoginInputsDTO.userEmail, completeLoginInputsDTO.verificationCode)

        response?.addCookie(createSessionTokenCookie(userSession))

        return userSession
    }

    data class StartLoginInputsDTO(
        val userEmail: String,
    )

    data class CompleteLoginInputsDTO(
        val userEmail: String,
        val verificationCode: String,
    )

    private fun createSessionTokenCookie(userSession: UserSession): Cookie {
        val cookie = Cookie("sessionToken", userSession.token)

        val secondsUntilSessionExpiration = Duration.between(Instant.now(), userSession.validTo).seconds.toInt()

        cookie.maxAge = secondsUntilSessionExpiration
        cookie.path = "/"

        // TODO: Use secure http-only cookie

        return cookie
    }
}
