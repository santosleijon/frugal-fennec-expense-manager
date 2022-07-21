package com.github.santosleijon.frugalfennecbackend.users.domain

interface RandomEmailVerificationCodeGenerator {
    fun generate(): String
}
