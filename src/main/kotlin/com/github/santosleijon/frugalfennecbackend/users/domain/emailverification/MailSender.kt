package com.github.santosleijon.frugalfennecbackend.users.domain.emailverification

import com.sendgrid.helpers.mail.Mail

interface MailSender {
    fun send(mail: Mail)
}