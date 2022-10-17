package com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.common.eventpubsub.EventServer
import com.github.santosleijon.frugalfennecbackend.common.eventpubsub.EventSubscriber
import com.github.santosleijon.frugalfennecbackend.users.domain.User
import com.github.santosleijon.frugalfennecbackend.users.domain.events.UserCreatedEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjection
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class UserProjector @Autowired constructor(
    eventServer: EventServer,
    private val userProjectionRepository: UserProjectionRepository,
) : EventSubscriber(User.aggregateName, eventServer) {

    override fun handleEvent(event: DomainEvent) {
        when (event) {
            is UserCreatedEvent -> {
                val projection = UserProjection(
                    id = event.aggregateId,
                    email = event.email,
                    created = event.date,
                    version = event.version,
                )

                userProjectionRepository.save(projection)
            }
        }
    }
}
