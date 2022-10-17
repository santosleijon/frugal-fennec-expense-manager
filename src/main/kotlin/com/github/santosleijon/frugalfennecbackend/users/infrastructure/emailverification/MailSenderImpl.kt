package com.github.santosleijon.frugalfennecbackend.users.infrastructure.emailverification

import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.MailSender
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory

@Component
@Suppress("unused")
class MailSenderImpl : MailSender {
    private val sendGridApiKey: String? = System.getenv("SENDGRID_API_KEY")
    private val sendGrid: SendGrid? = if (sendGridApiKey != null) {
            SendGrid(sendGridApiKey)
        } else {
            null
        }

    private var logger = LoggerFactory.getLogger(this::class.java)

    override fun send(mail: Mail) {
        if (sendGrid == null) {
            logger.warn("SendGrid API key not configured. Email will not be sent to {}", mail.personalization.first().tos.first().email)
            return
        }

        val request = Request()

        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()

        val response = sendGrid.api(request)

        if (response.statusCode != HttpStatus.ACCEPTED.value()) {
            error("Failed to send email (statusCode = ${response.statusCode}): ${response.body}")
        }
    }
}
