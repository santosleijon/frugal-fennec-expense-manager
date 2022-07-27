package com.github.santosleijon.frugalfennecbackend.users.domain.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class UserSessionCreatedEvent(
    val id: UUID,
    override val version: Int,
    override val userId: UUID,
    val token: String,
    val issued: Instant,
    val validTo: Instant,
) : DomainEvent {
    override val eventId = id
    override val aggregateName = UserSession.aggregateName
    override val aggregateId = id
    override val date: Instant = Instant.now()
}
