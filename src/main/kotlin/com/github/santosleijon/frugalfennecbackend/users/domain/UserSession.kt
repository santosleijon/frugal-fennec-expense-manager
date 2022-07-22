package com.github.santosleijon.frugalfennecbackend.users.domain

import java.time.Instant

data class UserSession(
    val userEmail: String,
    val token: String,
    val issued: Instant,
    val validTo: Instant,
)
