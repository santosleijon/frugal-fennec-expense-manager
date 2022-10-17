package com.github.santosleijon.frugalfennecbackend.users.domain.emailverification

import java.time.Instant

data class EmailVerificationCode (
    val email: String,
    val verificationCode: String,
    val issued: Instant,
    val validTo: Instant,
    val consumed: Instant? = null,
)
