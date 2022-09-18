package com.github.santosleijon.frugalfennecbackend.users.application.queries

import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetCurrentUserSessionQuery @Autowired constructor(
    private val userSessionProjectionRepository: UserSessionProjectionRepository,
    private val userProjectionRepository: UserProjectionRepository,
) {
    fun handle(userSessionToken: String?): UserResource.GetCurrentUserSessionDTO {
        val noValidUserSessionResponse = UserResource.GetCurrentUserSessionDTO(
            hasValidUserSession = false
        )

        if (userSessionToken == null) {
            return noValidUserSessionResponse
        }

        val userSessionProjection = userSessionProjectionRepository.findByToken(userSessionToken)
            ?: return noValidUserSessionResponse

        val userProjection = userProjectionRepository.findById(userSessionProjection.userId)
            ?: return noValidUserSessionResponse

        return UserResource.GetCurrentUserSessionDTO(
            hasValidUserSession = true,
            userId = userSessionProjection.id,
            email = userProjection.email,
            userSession = userSessionProjection,
        )
    }
}
