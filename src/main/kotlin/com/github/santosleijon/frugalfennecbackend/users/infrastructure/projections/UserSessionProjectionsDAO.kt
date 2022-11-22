package com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.santosleijon.frugalfennecbackend.common.utils.toZuluLocalDateTime
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.Instant
import java.util.UUID

@Component
class UserSessionProjectionsDAO @Autowired constructor(
    private val template: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
) {
    fun upsert(userSessionProjection: UserSessionProjection) {
        val paramMap: Map<String, Any> = mapOf(
            "id" to userSessionProjection.id,
            "user_id" to userSessionProjection.userId,
            "valid_to" to userSessionProjection.validTo.toZuluLocalDateTime(),
            "data" to objectMapper.writeValueAsString(userSessionProjection),
            "version" to userSessionProjection.version,
        )

        template.update("""
            INSERT INTO user_session_projections (
                id,
                user_id,
                valid_to,
                data,
                version
            )
            VALUES (
                :id,
                :user_id,
                :valid_to,
                :data::jsonb,
                :version
            )
            ON CONFLICT (id)
            DO
                UPDATE SET
                    user_id = :user_id,
                    valid_to = :valid_to,
                    data = :data::jsonb,
                    version = :version
        """.trimIndent(), paramMap)
    }

    fun findValidSessionById(sessionId: UUID): UserSessionProjection? {
        val paramMap: Map<String, Any> = mapOf(
            "id" to sessionId,
            "current_time" to Instant.now().toZuluLocalDateTime(),
        )

        return template.query("""
            SELECT
                data
            FROM
                user_session_projections
            WHERE
                id = :id AND
                valid_to >= :current_time
            LIMIT 1
        """.trimIndent(), paramMap, RowMapping(objectMapper)
        ).firstOrNull()
    }

    fun findByUserId(userId: UUID): List<UserSessionProjection> {
        val paramMap: Map<String, UUID> = mapOf(
            "userId" to userId,
        )

        return template.query("""
            SELECT
                data
            FROM
                user_session_projections
            WHERE
                user_id = :userId
            LIMIT 1
        """.trimIndent(), paramMap, RowMapping(objectMapper))
    }

    fun deleteByUserId(userId: UUID) {
        val paramMap: Map<String, UUID> = mapOf(
            "user_id" to userId,
        )

        template.update("""
            DELETE FROM user_session_projections
            WHERE user_id = :user_id
        """.trimIndent(), paramMap)
    }

    class RowMapping constructor(
        private val objectMapper: ObjectMapper,
    ): RowMapper<UserSessionProjection> {
        override fun mapRow(resultSet: ResultSet, arg1: Int): UserSessionProjection {
            val projectionAsJsonString = resultSet.getString("data")
            return objectMapper.readValue(projectionAsJsonString, UserSessionProjection::class.java)
        }
    }
}
