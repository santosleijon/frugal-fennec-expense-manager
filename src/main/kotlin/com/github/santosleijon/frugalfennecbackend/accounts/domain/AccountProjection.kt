package com.github.santosleijon.frugalfennecbackend.accounts.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.UUID

@JsonIgnoreProperties("version")
data class AccountProjection(
    val id: UUID,
    val name: String,
    val expenses: List<Expense>,
    val version: Int,
)

fun Account.toAccountProjection(): AccountProjection = AccountProjection(
    id = this.id,
    name = this.name ?: "",
    expenses = this.expenses,
    version = this.version,
)