package com.github.santosleijon.frugalfennecbackend.users.domain

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidSessionToken
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAuthorizer constructor(
    private val userSessionProjectionRepository: UserSessionProjectionRepository,
) {
    fun validateUserSessionAndGetUserId(sessionToken: String): UUID {
        val validUserSession = userSessionProjectionRepository.findValidSessionByToken(sessionToken)
            ?: throw InvalidSessionToken(sessionToken)

        return validUserSession.userId
    }
}
