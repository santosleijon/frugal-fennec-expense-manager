package com.github.santosleijon.frugalfennecbackend.users.infrastructure.emailverification

import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCode
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.EmailVerificationCodeRepository
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@Suppress("unused")
class EmailVerificationCodeRepositoryImpl constructor(
    private val emailVerificationCodesDAO: EmailVerificationCodesDAO,
) : EmailVerificationCodeRepository {

    override fun save(emailVerificationCode: EmailVerificationCode): EmailVerificationCode {
        emailVerificationCodesDAO.insert(emailVerificationCode)

        return emailVerificationCode
    }

    override fun isValid(email: String, verificationCode: String, currentTime: Instant): Boolean {
        return emailVerificationCodesDAO.isValid(email, verificationCode, currentTime)
    }

    override fun markAsConsumed(email: String, verificationCode: String) {
        emailVerificationCodesDAO.markAsConsumed(email, verificationCode)
    }

    override fun deleteUnconsumed(email: String) {
        emailVerificationCodesDAO.deleteUnconsumed(email)
    }

    override fun countValidUnconsumed(): Int {
        return emailVerificationCodesDAO.countValidUnconsumed()
    }

    override fun countUnconsumedByEmail(email: String): Int {
        return emailVerificationCodesDAO.countUnconsumedByEmail(email)
    }
}
