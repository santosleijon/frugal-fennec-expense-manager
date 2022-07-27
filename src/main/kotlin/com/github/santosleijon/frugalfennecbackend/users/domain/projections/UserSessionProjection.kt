package com.github.santosleijon.frugalfennecbackend.users.domain.projections

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Instant
import java.util.*

@JsonIgnoreProperties("version")
data class UserSessionProjection (
    val id: UUID,
    val version: Int,
    val userId: UUID,
    val token: String,
    val issued: Instant,
    val validTo: Instant,
)
