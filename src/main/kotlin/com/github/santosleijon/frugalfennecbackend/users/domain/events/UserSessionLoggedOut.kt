package com.github.santosleijon.frugalfennecbackend.users.domain.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class UserSessionLoggedOutEvent(
    val userSessionId: UUID,
    override val version: Int,
    override val userId: UUID,
) : DomainEvent {
    override val eventId = UUID.randomUUID()
    override val aggregateName = UserSession.aggregateName
    override val aggregateId = userSessionId
    override val date: Instant = Instant.now()
}
