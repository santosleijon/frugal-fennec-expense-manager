package com.github.santosleijon.frugalfennecbackend.users.domain

import java.time.Instant
import java.util.*

data class UserSession(
    val userId: UUID,
    val token: String,
    val issued: Instant,
    val validTo: Instant,
)
