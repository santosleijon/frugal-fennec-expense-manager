package com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.common.eventpubsub.EventServer
import com.github.santosleijon.frugalfennecbackend.common.eventpubsub.EventSubscriber
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.events.UserSessionCreatedEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjection
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class UserSessionProjector @Autowired constructor(
    eventServer: EventServer,
    private val userSessionProjectionRepository: UserSessionProjectionRepository,
) : EventSubscriber(UserSession.aggregateName, eventServer) {

    override fun handleEvent(event: DomainEvent) {
        when (event) {
            is UserSessionCreatedEvent -> {
                val projection = UserSessionProjection(
                    id = event.aggregateId,
                    userId = event.userId,
                    token = event.token,
                    issued = event.issued,
                    validTo = event.validTo,
                    version = event.version,
                )

                userSessionProjectionRepository.save(projection)
            }
        }
    }
}
