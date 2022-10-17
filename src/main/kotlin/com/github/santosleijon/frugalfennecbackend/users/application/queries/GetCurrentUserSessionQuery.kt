package com.github.santosleijon.frugalfennecbackend.users.application.queries

import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.users.application.api.UserResource
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetCurrentUserSessionQuery @Autowired constructor(
    private val userSessionProjectionRepository: UserSessionProjectionRepository,
    private val userProjectionRepository: UserProjectionRepository,
) : Command<GetCurrentUserSessionQuery.Input, UserResource.GetCurrentUserSessionResultDTO> {

    data class Input(
        val sessionId: UUID?,
    )

    override fun execute(input: Input): UserResource.GetCurrentUserSessionResultDTO {
        val noValidUserSessionResponse = UserResource.GetCurrentUserSessionResultDTO(
            hasValidUserSession = false
        )

        if (input.sessionId == null) {
            return noValidUserSessionResponse
        }

        val userSessionProjection = userSessionProjectionRepository.findValidSessionById(input.sessionId)
            ?: return noValidUserSessionResponse

        val userProjection = userProjectionRepository.findById(userSessionProjection.userId)
            ?: return noValidUserSessionResponse

        return UserResource.GetCurrentUserSessionResultDTO(
            hasValidUserSession = true,
            userId = userSessionProjection.id,
            email = userProjection.email,
            userSession = userSessionProjection,
        )
    }
}
