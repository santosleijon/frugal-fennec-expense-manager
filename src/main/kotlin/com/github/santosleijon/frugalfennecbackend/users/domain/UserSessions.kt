package com.github.santosleijon.frugalfennecbackend.users.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class UserSessions @Autowired constructor(
    private val userSessionRepository: UserSessionRepository,
) {
    // TODO: Move to CompleteLoginCommandHandler
    fun create(userId: UUID): UserSession {
        val sessionId = UUID.randomUUID() // Cryptographically strong pseudo random number
        val issuedDate = Instant.now()
        val expirationDate = Instant.now().plus(Duration.ofDays(7))

        val userSession = UserSession(
            id = sessionId,
            userId = userId,
            issued = issuedDate,
            validTo = expirationDate,
        )

        return userSessionRepository.save(userSession)
            ?: error("Failed to save new user session")
    }
}
