package com.github.santosleijon.frugalfennecbackend.users.domain

import com.sendgrid.helpers.mail.Mail

interface MailSender {
    fun send(mail: Mail)
}