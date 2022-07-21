package com.github.santosleijon.frugalfennecbackend.users.domain

import java.time.Instant

interface EmailVerificationCodeRepository {
    fun save(emailVerificationCode: EmailVerificationCode): EmailVerificationCode
    fun isValid(email: String, verificationCode: String, currentTime: Instant): Boolean
}
