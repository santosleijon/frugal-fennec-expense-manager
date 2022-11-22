package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.infrastructure.projections.AccountProjectionsDAO
import com.github.santosleijon.frugalfennecbackend.common.EventStoreDAO
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections.UserProjectionsDAO
import com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections.UserSessionProjectionsDAO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class DeleteUserCommand (
    private val userSessionRepository: UserSessionRepository,
    private val userProjectionRepository: UserProjectionRepository,
    private val eventStoreDAO: EventStoreDAO,
    private val userProjectionsDAO: UserProjectionsDAO,
    private val userSessionProjectionsDAO: UserSessionProjectionsDAO,
    private val accountsProjectionsDAO: AccountProjectionsDAO,
): Command<DeleteUserCommand.Input, Unit> {

    private var logger = LoggerFactory.getLogger(this::class.java)

    data class Input(
        val sessionId: UUID,
    )

    override fun execute(input: Input) {
        val userSession = userSessionRepository.findById(input.sessionId)
            ?: return

        val userId = userSession.userId!!
        val userEmail = userProjectionRepository.findById(userId)!!.email

        // Hard-delete all event store events for the user
        eventStoreDAO.deleteByUserId(userId)

        // Hard-delete all projections for the user data
        userProjectionsDAO.delete(userId)
        userSessionProjectionsDAO.deleteByUserId(userId)
        accountsProjectionsDAO.deleteByUserId(userId)

        logger.info("Deleted user {} with email {} during session {}", userId, userEmail, input.sessionId)
    }
}
