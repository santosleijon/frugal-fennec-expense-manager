package com.github.santosleijon.frugalfennecbackend.users.domain.events

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.User
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
class UserCreatedEvent(
    val id: UUID,
    override val version: Int,
    val email: String,
) : DomainEvent {
    override val eventId = id
    override val aggregateName = User.aggregateName
    override val aggregateId = id
    override val date: Instant = Instant.now()
}
