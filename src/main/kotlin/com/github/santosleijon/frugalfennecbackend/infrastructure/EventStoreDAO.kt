package com.github.santosleijon.frugalfennecbackend.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class EventStoreDAO @Autowired constructor(
    private val template: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
) {
    fun findByAggregateId(aggregateId: UUID): MutableList<DomainEvent> {
        val paramMap: Map<String, Any> = mapOf(
            "aggregate_id" to aggregateId,
        )

        return template.query("""
            SELECT
                data
            FROM
                event_store
            WHERE
                aggregate_id = :aggregate_id
        """.trimIndent(), paramMap, EventMapping(objectMapper)
        )
    }

    fun insert(event: DomainEvent) {
        val paramMap: Map<String, Any> = mapOf(
            "event_id" to event.eventId,
            "aggregate_name" to event.aggregateName,
            "aggregate_id" to event.aggregateId,
            "event_date" to event.date.toZuluLocalDateTime(),
            "version" to event.version,
            "data" to objectMapper.writeValueAsString(event),
        )

        // TODO: Handle error when result code <> 0
        template.update("""
            INSERT INTO event_store (
                event_id,
                aggregate_name,
                aggregate_id,
                event_date,
                version,
                data
            )
            VALUES (
                :event_id,
                :aggregate_name,
                :aggregate_id,
                :event_date,
                :version,
                :data::jsonb
            )
        """.trimIndent(), paramMap)
    }

    class EventMapping constructor(
        private val objectMapper: ObjectMapper,
    ) : RowMapper<DomainEvent> {
        override fun mapRow(resultSet: ResultSet, arg1: Int): DomainEvent {
            val eventAsJsonString = resultSet.getString("data")
            return objectMapper.readValue(eventAsJsonString, DomainEvent::class.java)
        }
    }

    private fun Instant.toZuluLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.of("Z"))
}
