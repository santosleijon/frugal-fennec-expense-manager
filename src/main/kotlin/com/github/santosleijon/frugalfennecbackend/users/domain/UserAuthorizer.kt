package com.github.santosleijon.frugalfennecbackend.users.domain

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidSessionId
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAuthorizer constructor(
    private val userSessionProjectionRepository: UserSessionProjectionRepository,
) {
    fun validateUserSessionAndGetUserId(sessionId: UUID): UUID {
        val validUserSession = userSessionProjectionRepository.findValidSessionById(sessionId)
            ?: throw InvalidSessionId(sessionId)

        return validUserSession.userId
    }
}
