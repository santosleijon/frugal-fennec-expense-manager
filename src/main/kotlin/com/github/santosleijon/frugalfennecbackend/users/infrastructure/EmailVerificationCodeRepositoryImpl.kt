package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.github.santosleijon.frugalfennecbackend.users.domain.EmailVerificationCode
import com.github.santosleijon.frugalfennecbackend.users.domain.EmailVerificationCodeRepository
import org.springframework.stereotype.Component
import java.time.Instant

@Component
@Suppress("unused")
class EmailVerificationCodeRepositoryImpl constructor(
    private val emailVerificationCodesDAO: EmailVerificationCodesDAO,
) : EmailVerificationCodeRepository {

    override fun save(emailVerificationCode: EmailVerificationCode): EmailVerificationCode {
        emailVerificationCodesDAO.upsert(emailVerificationCode)

        return emailVerificationCode
    }

    override fun isValid(email: String, verificationCode: String, currentTime: Instant): Boolean {
        return emailVerificationCodesDAO.isValid(email, verificationCode, currentTime)
    }
}
