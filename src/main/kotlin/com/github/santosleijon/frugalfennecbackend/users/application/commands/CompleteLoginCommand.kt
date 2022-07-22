package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailVerificationCodeError
import com.github.santosleijon.frugalfennecbackend.users.domain.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class CompleteLoginCommand @Autowired constructor(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
    private val userSessions: UserSessions,
) {

    private var logger = LoggerFactory.getLogger(this::class.java)

    fun handle(userEmail: String, verificationCode: String): UserSession {
        if (!emailVerificationCodeRepository.isValid(userEmail, verificationCode, Instant.now())) {
            throw InvalidEmailVerificationCodeError(userEmail)
        }

        // TODO: Fetch existing user, or save new user if not existing
        val userId = UUID.randomUUID()

        val userSession = userSessions.create(userId)

        emailVerificationCodeRepository.markAsConsumed(userEmail, verificationCode)

        logger.info("Completed login for user $userEmail. Given session token: $userSession")

        return userSession
    }
}
