package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class LogoutCommand @Autowired constructor(
    private val userSessions: UserSessions,
    private val userSessionRepository: UserSessionRepository,
) : Command<LogoutCommand.Input, Unit> {

    private var logger = LoggerFactory.getLogger(this::class.java)

    data class Input(
        val userSessionToken: String,
    )

    override fun execute(input: Input) {
        val userSessionId = userSessions.getSessionIdFromSessionToken(input.userSessionToken)

        val userSession = userSessionRepository.findById(userSessionId)
            ?: return

        userSession.logout(userSession.userId!!)

        userSessionRepository.save(userSession)

        logger.info("Logged out user ${userSession.userId} from session $userSessionId")
    }
}
