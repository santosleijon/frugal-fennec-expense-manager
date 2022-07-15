package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.sendgrid.Response
import com.sendgrid.helpers.mail.Mail

interface MailSender {
    fun send(mail: Mail): Response
}