package com.github.santosleijon.frugalfennecbackend.accounts.domain.projections

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import java.util.UUID

@JsonIgnoreProperties("version")
data class AccountProjection(
    val id: UUID,
    val name: String,
    val expenses: List<Expense>,
    val version: Int,
)
