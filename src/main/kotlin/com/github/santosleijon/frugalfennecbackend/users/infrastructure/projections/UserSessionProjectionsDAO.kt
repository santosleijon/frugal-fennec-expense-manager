package com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class UserSessionProjectionsDAO @Autowired constructor(
    private val template: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
) {
    fun upsert(userSessionProjection: UserSessionProjection) {
        val paramMap: Map<String, Any> = mapOf(
            "id" to userSessionProjection.id,
            "user_id" to userSessionProjection.userId,
            "token" to userSessionProjection.token,
            "data" to objectMapper.writeValueAsString(userSessionProjection),
            "version" to userSessionProjection.version,
        )

        template.update("""
            INSERT INTO user_session_projections (
                id,
                user_id,
                token,
                data,
                version
            )
            VALUES (
                :id,
                :user_id,
                :token,
                :data::jsonb,
                :version
            )
            ON CONFLICT (id)
            DO
                UPDATE SET
                    user_id = :user_id,
                    token = :token,
                    data = :data::jsonb,
                    version = :version
        """.trimIndent(), paramMap)
    }

    fun findByToken(token: String): UserSessionProjection? {
        val paramMap: Map<String, String> = mapOf(
            "token" to token,
        )

        return template.query("""
            SELECT
                 data
            FROM
                user_session_projections
            WHERE
                token = :token
            LIMIT 1
        """.trimIndent(), paramMap, RowMapping(objectMapper)
        ).firstOrNull()
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
