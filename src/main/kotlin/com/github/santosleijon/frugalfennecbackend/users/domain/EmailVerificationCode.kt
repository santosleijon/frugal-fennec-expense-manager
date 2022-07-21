package com.github.santosleijon.frugalfennecbackend.users.domain

import java.time.Instant

data class EmailVerificationCode (
    val email: String,
    val verificationCode: String,
    val issued: Instant,
    val validTo: Instant,
)
