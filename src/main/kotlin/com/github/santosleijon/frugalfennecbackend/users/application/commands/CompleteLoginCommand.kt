package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailVerificationCodeError
import com.github.santosleijon.frugalfennecbackend.users.domain.*
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCodeRepository
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

    fun handle(userEmail: String, verificationCode: String): UserSession {
        if (!emailVerificationCodeRepository.isValid(userEmail, verificationCode, Instant.now())) {
            throw InvalidEmailVerificationCodeError(userEmail)
        }

        val existingUser = userProjectionRepository.findByEmail(userEmail)

        val userId: UUID?

        if (existingUser == null) {
            userId = UUID.randomUUID()

            val newUser = User(
                userId,
                userEmail,
            )

            userRepository.save(newUser)

            logger.info("New user for email $userEmail created. User ID: $userId")
        } else {
            userId = existingUser.id
        }

        val userSession = userSessions.create(userId!!)

        emailVerificationCodeRepository.markAsConsumed(userEmail, verificationCode)

        logger.info("Completed login for user $userEmail. Given session token: $userSession")

        return userSession
    }
}
