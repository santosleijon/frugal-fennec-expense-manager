package com.github.santosleijon.frugalfennecbackend.users.infrastructure.emailverification

import com.github.santosleijon.frugalfennecbackend.users.domain.emailverification.RandomEmailVerificationCodeGenerator
import org.springframework.stereotype.Component
import kotlin.math.floor

@Component
@Suppress("unused")
class RandomEmailVerificationCodeGeneratorImpl : RandomEmailVerificationCodeGenerator {
    override fun generate(): String {
        val maxLimit = 9999
        val minLimit = 1000

        return floor(Math.random() * (maxLimit - minLimit + 1) + minLimit)
            .toInt()
            .toString()
    }
}
