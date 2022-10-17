package com.github.santosleijon.frugalfennecbackend.bdd.mocks

import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.RandomEmailVerificationCodeGenerator
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Profile("test")
@Configuration
@Suppress("unused")
class MockRandomEmailVerificationCodeGeneratorConfiguration {
    @Bean
    @Primary
    fun randomEmailVerificationCodeGenerator(): RandomEmailVerificationCodeGenerator {
        return Mockito.mock(RandomEmailVerificationCodeGenerator::class.java)
    }
}

object MockRandomEmailVerificationCodeGenerator : RandomEmailVerificationCodeGenerator {

    private var verificationCode = ""

    fun init(mockedRandomEmailVerificationCodeGenerator: RandomEmailVerificationCodeGenerator) {
        whenever(mockedRandomEmailVerificationCodeGenerator.generate()).thenAnswer {
            generate()
        }
    }

    fun setVerificationCode(verificationCode: String) {
        this.verificationCode = verificationCode
    }

    override fun generate(): String {
        return verificationCode
    }
}
