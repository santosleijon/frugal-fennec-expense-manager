package com.github.santosleijon.frugalfennecbackend.users.application.commands

import com.github.santosleijon.frugalfennecbackend.users.infrastructure.MailSender
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.floor

@Component
class StartLoginCommand @Autowired constructor(
    private val mailSender: MailSender,
) {

    fun handle(userEmail: String) {
        val verificationCode = generateUniqueVerificationCode(userEmail)

        val verificationMail = createEmailVerificationMail(userEmail, verificationCode)

        val mailResponse = mailSender.send(verificationMail)

        println(mailResponse.body)
        println(mailResponse.headers)
        println(mailResponse.statusCode)
    }

    private fun generateUniqueVerificationCode(userEmail: String): String {
        val maxLimit = 9999
        val minLimit = 1000

        val verificationCode = floor(Math.random() * (maxLimit - minLimit + 1) + minLimit).toString()

        // TODO: Verify uniqueness by querying DB
        // TODO: Save verification code in DB
        return verificationCode
    }

    private fun createEmailVerificationMail(recipientAddress: String, verificationCode: String): Mail {
        val from = Email("santos.leijon@gmail.com")
        val subject = "Sending with SendGrid is Fun"
        val to = Email(recipientAddress)
        val content = Content("text/plain", "and easy to do anywhere, even with Java")
        val templateId = "d-bcf8c873247f4bb68f4551d54df9ec6b"

        val personalization = Personalization().also {
            it.addDynamicTemplateData("verificationCode", verificationCode)
        }

        return Mail(from, subject, to, content).also {
            it.templateId = templateId
            it.addPersonalization(personalization)
        }
    }
}