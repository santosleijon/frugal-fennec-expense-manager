package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailAddressError
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailVerificationCodeError
import com.github.santosleijon.frugalfennecbackend.users.domain.*
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCodeRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.isValidEmail
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
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
) {

    private var logger = LoggerFactory.getLogger(this::class.java)

    fun handle(email: String, verificationCode: String): UserSession {
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

        logger.info("Completed login for user $email. Given session token: $userSession")

        return userSession
    }
}
