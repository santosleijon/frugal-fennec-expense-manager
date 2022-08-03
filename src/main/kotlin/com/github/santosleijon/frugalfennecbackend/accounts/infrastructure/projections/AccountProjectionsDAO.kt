package com.github.santosleijon.frugalfennecbackend.accounts.infrastructure.projections

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.util.*

@Component
class AccountProjectionsDAO @Autowired constructor(
    private val template: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
) {
    fun findAll(): MutableList<AccountProjection> {
        return template.query("""
            SELECT
                data
            FROM
                account_projections
            ORDER BY
                account_name
        """.trimIndent(), ProjectionMapping(objectMapper)
        )
    }

    fun findById(accountId: UUID): AccountProjection? {
        val paramMap: Map<String, Any> = mapOf(
            "account_id" to accountId,
        )

        return template.query("""
            SELECT
                 data
            FROM
                account_projections
            WHERE
                account_id = :account_id
            LIMIT 1
        """.trimIndent(), paramMap, ProjectionMapping(objectMapper)
        ).firstOrNull()
    }

    fun findByName(name: String, userId: UUID): AccountProjection? {
        val paramMap: Map<String, Any> = mapOf(
            "account_name" to name,
        )

        return template.query("""
            SELECT
                 data
            FROM
                account_projections
            WHERE
                account_name = :account_name
            LIMIT 1
        """.trimIndent(), paramMap, ProjectionMapping(objectMapper)
        ).firstOrNull()
    }

    fun upsert(accountProjection: AccountProjection) {
        val paramMap: Map<String, Any> = mapOf(
            "account_id" to accountProjection.id,
            "user_id" to accountProjection.userId,
            "account_name" to accountProjection.name,
            "data" to objectMapper.writeValueAsString(accountProjection),
            "version" to accountProjection.version,
        )

        template.update("""
            INSERT INTO account_projections (
                account_id,
                user_id,
                account_name,
                data,
                version
            )
            VALUES (
                :account_id,
                :user_id,
                :account_name,
                :data::jsonb,
                :version
            )
            ON CONFLICT (account_id)
            DO
                UPDATE SET
                    account_name = :account_name,
                    user_id = :user_id,
                    data = :data::jsonb,
                    version = :version
        """.trimIndent(), paramMap)
    }

    fun delete(accountId: UUID) {
        val paramMap: Map<String, Any> = mapOf(
            "account_id" to accountId,
        )

        template.update("""
            DELETE FROM account_projections
            WHERE account_id = :account_id
        """.trimIndent(), paramMap)
    }

    class ProjectionMapping constructor(
        private val objectMapper: ObjectMapper,
    ) : RowMapper<AccountProjection> {
        override fun mapRow(resultSet: ResultSet, arg1: Int): AccountProjection {
            val projectionAsJsonString = resultSet.getString("data")
            return objectMapper.readValue(projectionAsJsonString, AccountProjection::class.java)
        }
    }
}
