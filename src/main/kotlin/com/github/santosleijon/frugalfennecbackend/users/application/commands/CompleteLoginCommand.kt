package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailVerificationCodeError
import com.github.santosleijon.frugalfennecbackend.users.domain.EmailVerificationCodeRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.SessionTokens
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CompleteLoginCommand @Autowired constructor(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
    private val sessionTokens: SessionTokens,
) {

    private var logger = LoggerFactory.getLogger(this::class.java)

    fun handle(userEmail: String, verificationCode: String): String {
        if (!emailVerificationCodeRepository.isValid(userEmail, verificationCode, Instant.now())) {
            throw InvalidEmailVerificationCodeError(userEmail)
        }

        // TODO: Create user if not existing

        val sessionToken = sessionTokens.createToken(userEmail)

        emailVerificationCodeRepository.markAsConsumed(userEmail, verificationCode)

        logger.info("Completed login for user $userEmail. Given session token: $sessionToken")

        return sessionToken
    }
}
