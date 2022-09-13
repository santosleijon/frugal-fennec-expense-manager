package com.github.santosleijon.frugalfennecbackend.users.domain

import com.github.santosleijon.frugalfennecbackend.common.AggregateRoot
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.events.UserSessionCreatedEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.events.UserSessionLoggedOutEvent
import java.time.Instant
import java.util.*

class UserSession(
    id: UUID,
    version: Int,
) : AggregateRoot(id, version) {

    var userId: UUID? = null
        private set

    var token: String? = null
        private set

    var issued: Instant? = null
        private set

    var validTo: Instant? = null
        private set

    companion object {
        const val aggregateName: String = "UserSession"

        fun loadFrom(events: List<DomainEvent>, id: UUID, version: Int): UserSession {
            val newUserSession = UserSession(id, version)

            for (event in events) {
                newUserSession.mutate(event)
            }

            return newUserSession
        }
    }

    constructor(
        id: UUID,
        userId: UUID,
        token: String,
        issued: Instant,
        validTo: Instant,
    ) : this(id = id, version = 0) {
        this.apply(
            UserSessionCreatedEvent(
                id,
                version,
                userId,
                token,
                issued,
                validTo,
            )
        )
    }

    override fun mutate(event: DomainEvent) {
        when (event) {
            is UserSessionCreatedEvent -> {
                userId = event.userId
                token = event.token
                issued = event.issued
                validTo = event.validTo
            }
            is UserSessionLoggedOutEvent -> {
                validTo = event.date
            }
        }
    }

    fun logout(actorUserId: UUID) {
        this.apply(
            UserSessionLoggedOutEvent(
                userSessionId = this.id,
                version = this.version+1,
                userId = actorUserId,
            )
        )
    }
}
