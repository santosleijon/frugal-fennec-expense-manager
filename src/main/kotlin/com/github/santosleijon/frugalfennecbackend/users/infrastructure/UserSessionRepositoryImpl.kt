package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.github.santosleijon.frugalfennecbackend.common.EventStore
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class UserSessionRepositoryImpl @Autowired constructor(
    private val eventStore: EventStore,
) : UserSessionRepository {

    override fun save(userSession: UserSession): UserSession? {
        userSession.pendingEvents.forEach {
            eventStore.append(it)
        }

        return findById(userSession.id)
    }

    override fun findById(id: UUID): UserSession? {
        val eventStream = eventStore.loadStream(id)

        if (eventStream.events.isEmpty()) {
            return null
        }

        return UserSession.loadFrom(eventStream.events, id, eventStream.version)
    }
}
