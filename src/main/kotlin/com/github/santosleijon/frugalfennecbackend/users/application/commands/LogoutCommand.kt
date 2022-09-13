package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.users.application.errors.InvalidEmailAddressError
import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.*
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessionRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.UserRepository
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessions
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.UUID

@Component
class LogoutCommand @Autowired constructor(
    private val userSessions: UserSessions,
    private val userSessionRepository: UserSessionRepository,
) {

    private var logger = LoggerFactory.getLogger(this::class.java)

    fun handle(userSessionToken: String) {
        val userSessionId = userSessions.getSessionIdFromSessionToken(userSessionToken)

        val userSession = userSessionRepository.findById(userSessionId)
            ?: return

        userSession.logout(userSession.userId!!)

        userSessionRepository.save(userSession)

        logger.info("Logged out user ${userSession.userId} from session $userSessionId")
    }
}
