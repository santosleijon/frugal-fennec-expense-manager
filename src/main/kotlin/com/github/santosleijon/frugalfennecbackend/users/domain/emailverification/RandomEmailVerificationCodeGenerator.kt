package com.github.santosleijon.frugalfennecbackend.users.domain.emailverification

interface RandomEmailVerificationCodeGenerator {
    fun generate(): String
}
