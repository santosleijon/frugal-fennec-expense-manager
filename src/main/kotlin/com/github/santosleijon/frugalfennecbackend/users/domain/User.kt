package com.github.santosleijon.frugalfennecbackend.users.domain

import com.github.santosleijon.frugalfennecbackend.common.AggregateRoot
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.users.domain.events.UserCreatedEvent
import java.util.*

class User(
    id: UUID,
    version: Int,
) : AggregateRoot(id, version) {

    var email: String? = null
        private set

    companion object {
        const val aggregateName: String = "User"

        fun loadFrom(events: List<DomainEvent>, id: UUID, version: Int): User {
            val newUser = User(id, version)

            for (event in events) {
                newUser.mutate(event)
            }

            return newUser
        }
    }

    constructor(
        id: UUID,
        email: String
    ) : this(id = id, version = 0) {
        this.apply(
            UserCreatedEvent(
                id,
                version,
                email,
            )
        )
    }

    override fun mutate(event: DomainEvent) {
        when (event) {
            is UserCreatedEvent -> {
                email = event.email
            }
        }
    }
}
