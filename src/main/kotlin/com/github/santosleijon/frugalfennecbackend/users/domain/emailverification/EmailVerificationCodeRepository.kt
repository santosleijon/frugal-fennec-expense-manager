package com.github.santosleijon.frugalfennecbackend.users.domain.emailverification

import java.time.Instant

interface EmailVerificationCodeRepository {
    fun save(emailVerificationCode: EmailVerificationCode): EmailVerificationCode
    fun isValid(email: String, verificationCode: String, currentTime: Instant): Boolean
    fun markAsConsumed(email: String, verificationCode: String)
    fun deleteUnconsumed(email: String)
    fun countValidUnconsumed(): Int
    fun countUnconsumedByEmail(email: String): Int
}
