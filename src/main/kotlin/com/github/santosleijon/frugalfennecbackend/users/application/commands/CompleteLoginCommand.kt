package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailAddressError
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailVerificationCodeError
import com.github.santosleijon.frugalfennecbackend.users.domain.*
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCodeRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.isValidEmail
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class CompleteLoginCommand @Autowired constructor(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
    private val userProjectionRepository: UserProjectionRepository,
    private val userRepository: UserRepository,
    private val userSessionRepository: UserSessionRepository,
) : Command<CompleteLoginCommand.Input, CompleteLoginCommand.Result> {

    private var logger = LoggerFactory.getLogger(this::class.java)

    data class Input(
        val email: String,
        val verificationCode: String,
    )

    data class Result(
        val userId: UUID,
        val userEmail: String,
        val userSession: UserSession,
    )

    override fun execute(input: Input): Result {
        val email = input.email
        val verificationCode = input.verificationCode

        if (!isValidEmail(email)) {
            throw InvalidEmailAddressError(email)
        }

        if (!emailVerificationCodeRepository.isValid(email, verificationCode, Instant.now())) {
            throw InvalidEmailVerificationCodeError(email)
        }

        val existingUser = userProjectionRepository.findByEmail(email)

        val userId = if (existingUser == null) {
            val newUser = User(
                UUID.randomUUID(),
                email,
            )

            userRepository.save(newUser)

            logger.info("New user for email $email created. User ID: {}", newUser.id)

            newUser.id
        } else {
            existingUser.id
        }

        val userSession = createUserSession(userId)

        emailVerificationCodeRepository.markAsConsumed(email, verificationCode)

        logger.info("Completed login for user {}. Created session {}", email, userSession.id)

        return Result(userId, email, userSession)
    }

    fun createUserSession(userId: UUID): UserSession {
        val sessionId = UUID.randomUUID() // Cryptographically strong pseudo random number
        val issuedDate = Instant.now()
        val expirationDate = Instant.now().plus(Duration.ofDays(7))

        val userSession = UserSession(
            id = sessionId,
            userId = userId,
            issued = issuedDate,
            validTo = expirationDate,
        )

        return userSessionRepository.save(userSession)
            ?: error("Failed to save new user session")
    }
}
