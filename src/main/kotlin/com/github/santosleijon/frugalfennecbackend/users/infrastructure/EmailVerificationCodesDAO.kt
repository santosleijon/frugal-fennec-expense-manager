package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.github.santosleijon.frugalfennecbackend.common.utils.toZuluLocalDateTime
import com.github.santosleijon.frugalfennecbackend.users.domain.EmailVerificationCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class EmailVerificationCodesDAO @Autowired constructor(
    private val template: NamedParameterJdbcTemplate,
) {

    fun upsert(emailVerificationCode: EmailVerificationCode) {
        val paramMap: Map<String, Any> = mapOf(
            "email" to emailVerificationCode.email,
            "verification_code" to emailVerificationCode.verificationCode,
            "issued" to emailVerificationCode.issued.toZuluLocalDateTime(),
            "valid_to" to emailVerificationCode.validTo.toZuluLocalDateTime(),
        )

        template.update("""
            INSERT INTO email_verification_codes (
                email,
                verification_code,
                issued,
                valid_to
            )
            VALUES (
                :email,
                :verification_code,
                :issued,
                :valid_to
            )
        """.trimIndent(), paramMap)
    }

    fun isValid(email: String, verificationCode: String, currentTime: Instant): Boolean {
        val paramMap: Map<String, Any> = mapOf(
            "email" to email,
            "verification_code" to verificationCode,
            "current_time" to currentTime.toZuluLocalDateTime(),
        )

        val validMatchesCount = template.queryForObject("""
            SELECT
                 count(*)
            FROM
                email_verification_codes
            WHERE
                email = :email AND
                verification_code = :verification_code AND
                issued <= :current_time AND
                valid_to > :current_time
        """.trimIndent(), paramMap, Int::class.java)

        return validMatchesCount != null && validMatchesCount > 0
    }
}
