package com.github.santosleijon.frugalfennecbackend.bdd.mocks

import com.github.santosleijon.frugalfennecbackend.users.domain.MailSender
import com.sendgrid.helpers.mail.Mail
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Profile("test")
@Configuration
@Suppress("unused")
class MockMailSenderBeanConfiguration {
    @Bean
    @Primary
    fun mailSender(): MailSender {
        return Mockito.mock(MailSender::class.java)
    }
}

object MockMailSender : MailSender {

    val sentEmails: MutableList<Mail> = emptyList<Mail>().toMutableList()

    fun init(mockedMailSender: MailSender) {
        sentEmails.clear()

        whenever(mockedMailSender.send(org.mockito.kotlin.any())).thenAnswer {
            val mail = it.arguments[0] as Mail
            send(mail)
        }
    }

    override fun send(mail: Mail) {
        sentEmails.add(mail)
    }
}
