package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailAddressError
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailVerificationCodeError
import com.github.santosleijon.frugalfennecbackend.users.domain.*
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCodeRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.isValidEmail
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class CompleteLoginCommand @Autowired constructor(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
    private val userSessions: UserSessions,
    private val userProjectionRepository: UserProjectionRepository,
    private val userRepository: UserRepository,
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

        val userId: UUID?

        if (existingUser == null) {
            userId = UUID.randomUUID()

            val newUser = User(
                userId,
                email,
            )

            userRepository.save(newUser)

            logger.info("New user for email $email created. User ID: $userId")
        } else {
            userId = existingUser.id
        }

        val userSession = userSessions.create(userId!!)

        emailVerificationCodeRepository.markAsConsumed(email, verificationCode)

        logger.info("Completed login for user $email. Given session token: ${userSession.token}")

        return Result(userId, email, userSession)
    }
}
