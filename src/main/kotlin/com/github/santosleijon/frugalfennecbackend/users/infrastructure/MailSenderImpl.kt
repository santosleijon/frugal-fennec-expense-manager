package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class MailSenderImpl : MailSender {
    private val sendGridApiKey: String = System.getenv("SENDGRID_API_KEY")
    private val sendGrid = SendGrid(sendGridApiKey)

    override fun send(mail: Mail) {
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
