package com.github.santosleijon.frugalfennecbackend.users.application.commands

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
class AbortLoginCommand @Autowired constructor(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
) {

    private var logger = LoggerFactory.getLogger(this::class.java)

    // TODO: All of these "handle" command should be called "execute" since these classes are not command handlers
    fun handle(email: String) {
        if (!isValidEmail(email)) {
            throw InvalidEmailAddressError(email)
        }
        
        emailVerificationCodeRepository.deleteUnconsumed(email)

        logger.info("Aborted login for user $email")

        return
    }
}
