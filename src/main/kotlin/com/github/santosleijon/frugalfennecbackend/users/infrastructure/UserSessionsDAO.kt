package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.github.santosleijon.frugalfennecbackend.common.utils.toZuluLocalDateTime
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.*

@Component
class UserSessionsDAO @Autowired constructor(
    private val template: NamedParameterJdbcTemplate,
) {
    fun insert(userSession: UserSession) {
        val paramMap: Map<String, Any?> = mapOf(
            "user_id" to userSession.userId,
            "token" to userSession.token,
            "issued" to userSession.issued.toZuluLocalDateTime(),
            "valid_to" to userSession.validTo.toZuluLocalDateTime(),
        )

        template.update("""
            INSERT INTO user_sessions (
                user_id,
                token,
                issued,
                valid_to
            )
            VALUES (
                :user_id,
                :token,
                :issued,
                :valid_to
            )
        """.trimIndent(), paramMap)
    }

    fun getByToken(token: String): UserSession? {
        val paramMap: Map<String, String> = mapOf(
            "token" to token,
        )

        return template.query("""
            SELECT
                 user_id,
                 token,
                 issued,
                 valid_to
            FROM
                user_sessions
            WHERE
                token = :token
            LIMIT 1
        """.trimIndent(), paramMap, RowMapping()
        ).firstOrNull()
    }

    class RowMapping: RowMapper<UserSession> {
        override fun mapRow(resultSet: ResultSet, arg1: Int): UserSession {
            val userId = UUID.fromString(resultSet.getString("user_id"))
            val token = resultSet.getString("token")
            val issued = resultSet.getTimestamp("issued").toInstant()
            val validTo = resultSet.getTimestamp("valid_to").toInstant()

            return UserSession(
                userId = userId,
                token = token,
                issued = issued,
                validTo = validTo,
            )
        }
    }
}
