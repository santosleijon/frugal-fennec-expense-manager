package com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.*

@Component
class UserProjectionsDAO @Autowired constructor(
    private val template: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
) {
    fun upsert(userProjection: UserProjection) {
        val paramMap: Map<String, Any> = mapOf(
            "id" to userProjection.id,
            "email" to userProjection.email,
            "data" to objectMapper.writeValueAsString(userProjection),
            "version" to userProjection.version,
        )

        template.update("""
            INSERT INTO user_projections (
                id,
                email,
                data,
                version
            )
            VALUES (
                :id,
                :email,
                :data::jsonb,
                :version
            )
            ON CONFLICT (id)
            DO
                UPDATE SET
                    email = :email,
                    data = :data::jsonb,
                    version = :version
        """.trimIndent(), paramMap)
    }

    fun findByEmail(email: String): UserProjection? {
        val paramMap: Map<String, String> = mapOf(
            "email" to email,
        )

        return template.query("""
            SELECT
                 data
            FROM
                user_projections
            WHERE
                email = :email
            LIMIT 1
        """.trimIndent(), paramMap, RowMapping(objectMapper)
        ).firstOrNull()
    }

    fun findById(id: UUID): UserProjection? {
        val paramMap: Map<String, Any> = mapOf(
            "id" to id,
        )

        return template.query("""
            SELECT
                 data
            FROM
                user_projections
            WHERE
                id = :id
            LIMIT 1
        """.trimIndent(), paramMap, RowMapping(objectMapper)
        ).firstOrNull()
    }

    class RowMapping constructor(
        private val objectMapper: ObjectMapper,
    ) : RowMapper<UserProjection> {
        override fun mapRow(resultSet: ResultSet, arg1: Int): UserProjection {
            val projectionAsJsonString = resultSet.getString("data")
            return objectMapper.readValue(projectionAsJsonString, UserProjection::class.java)
        }
    }
}
