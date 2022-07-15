package com.github.santosleijon.frugalfennecbackend.bdd.mocks

import com.github.santosleijon.frugalfennecbackend.users.infrastructure.MailSender
import com.sendgrid.Response
import com.sendgrid.helpers.mail.Mail
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus

object MockMailSender : MailSender {
    val sentEmails: MutableList<Mail> = emptyList<Mail>().toMutableList()

    private val successfulResponse = Response(
        HttpStatus.ACCEPTED.value(),
        "{}",
        emptyMap(),
    )

    fun init(mockedMailSender: MailSender) {
        sentEmails.clear()

        whenever(mockedMailSender.send(org.mockito.kotlin.any())).thenAnswer {
            val mail = it.arguments[0] as Mail
            send(mail)
        }
    }

    override fun send(mail: Mail): Response {
        sentEmails.add(mail)

        return successfulResponse
    }
}