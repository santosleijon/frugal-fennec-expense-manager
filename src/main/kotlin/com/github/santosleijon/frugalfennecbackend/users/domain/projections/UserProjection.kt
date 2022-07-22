package com.github.santosleijon.frugalfennecbackend.users.domain.projections

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.Instant
import java.util.*

@JsonIgnoreProperties("version")
data class UserProjection(
    val id: UUID,
    val email: String,
    val created: Instant,
    val version: Int,
)
