package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.github.santosleijon.frugalfennecbackend.common.EventStore
import com.github.santosleijon.frugalfennecbackend.users.domain.User
import com.github.santosleijon.frugalfennecbackend.users.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Suppress("unused")
class UserRepositoryImpl @Autowired constructor(
    private val eventStore: EventStore,
) : UserRepository {

    override fun save(user: User) {
        TODO("Not yet implemented")
    }

    override fun getById(id: UUID): User? {
        val eventStream = eventStore.loadStream(id)

        if (eventStream.events.isEmpty()) {
            return null
        }

        return User.loadFrom(eventStream.events, id, eventStream.version)
    }

    override fun getByEmail(email: String): User? {
        TODO("Not yet implemented")
    }
}
