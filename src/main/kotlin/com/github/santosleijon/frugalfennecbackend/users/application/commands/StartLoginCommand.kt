package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailAddressError
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.*
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import MaxNumberOfStartedLoginsReached
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command

@Component
class StartLoginCommand @Autowired constructor(
    private val randomEmailVerificationCodeGenerator: RandomEmailVerificationCodeGenerator,
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
    private val mailSender: MailSender
) : Command<StartLoginCommand.Input, Unit> {

    companion object {
        const val MAX_NUMBER_OF_STARTED_LOGINS_ALLOWED = 10
    }

    private var logger = LoggerFactory.getLogger(this::class.java)

    data class Input(
        val email: String,
    )

    override fun execute(input: Input) {
        val email = input.email

        if (!isValidEmail(email)) {
            throw InvalidEmailAddressError(email)
        }

        if (emailVerificationCodeRepository.countValidUnconsumed() >= MAX_NUMBER_OF_STARTED_LOGINS_ALLOWED) {
            throw MaxNumberOfStartedLoginsReached()
        }

        val emailVerificationCode = createEmailVerificationCode(email)

        val verificationMail = createEmailVerificationMail(email, emailVerificationCode.verificationCode)

        mailSender.send(verificationMail)

        logger.info("Started login for {}. Sent email verification code: {}", email, emailVerificationCode.verificationCode)
    }

    private fun createEmailVerificationCode(email: String): EmailVerificationCode {
        val uniqueVerificationCode = randomEmailVerificationCodeGenerator.generate()

        val emailVerificationCode = EmailVerificationCode(
            email = email,
            verificationCode = uniqueVerificationCode,
            issued = Instant.now(),
            validTo = Instant.now().plus(Duration.ofMinutes(1)),
        )

        emailVerificationCodeRepository.save(emailVerificationCode)

        return emailVerificationCode
    }

    private fun createEmailVerificationMail(recipientAddress: String, verificationCode: String): Mail {
        val to = Email(recipientAddress)

        val personalization = Personalization().also {
            it.addTo(to)
            it.addDynamicTemplateData("verificationCode", verificationCode)
        }

        return Mail().also {
            it.from = Email("santos.leijon@gmail.com")
            it.templateId = "d-bcf8c873247f4bb68f4551d54df9ec6b"
            it.addPersonalization(personalization)
        }
    }
}
