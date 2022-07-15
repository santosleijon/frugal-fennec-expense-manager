package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class MailSenderImpl : MailSender {
    private val sendGridApiKey: String = System.getenv("SENDGRID_API_KEY")
    private val sendGrid = SendGrid(sendGridApiKey)

    override fun send(mail: Mail): Response {
        val request = Request()

        request.method = Method.POST
        request.endpoint = "mail/send"
        request.body = mail.build()

        return sendGrid.api(request)
    }
}
