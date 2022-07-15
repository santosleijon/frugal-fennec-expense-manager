package com.github.santosleijon.frugalfennecbackend.bdd.mocks

import com.github.santosleijon.frugalfennecbackend.users.infrastructure.MailSender
import org.mockito.Mockito
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
