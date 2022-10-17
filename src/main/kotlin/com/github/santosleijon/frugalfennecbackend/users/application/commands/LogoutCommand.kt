package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogoutCommand @Autowired constructor(
    private val userSessionRepository: UserSessionRepository,
) : Command<LogoutCommand.Input, Unit> {

    private var logger = LoggerFactory.getLogger(this::class.java)

    data class Input(
        val sessionId: UUID,
    )

    override fun execute(input: Input) {
        val userSession = userSessionRepository.findById(input.sessionId)
            ?: return

        userSession.logout(userSession.userId!!)

        userSessionRepository.save(userSession)

        logger.info("Logged out user {} from session {}", userSession.userId, input.sessionId)
    }
}
