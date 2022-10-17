package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailAddressError
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCodeRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.isValidEmail
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AbortLoginCommand @Autowired constructor(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
): Command<AbortLoginCommand.Input, Unit> {

    private var logger = LoggerFactory.getLogger(this::class.java)

    data class Input(
        val email: String,
    )

    override fun execute(input: Input) {
        val email = input.email

        if (!isValidEmail(email)) {
            throw InvalidEmailAddressError(email)
        }

        emailVerificationCodeRepository.deleteUnconsumed(email)

        logger.info("Aborted login for user {}", email)

        return
    }
}
