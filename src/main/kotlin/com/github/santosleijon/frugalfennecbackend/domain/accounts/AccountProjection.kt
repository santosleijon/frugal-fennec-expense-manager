package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.UUID

@JsonIgnoreProperties("version")
data class AccountProjection(
    val id: UUID,
    val name: String,
    val expenses: List<Expense>,
    val version: Int,
)
