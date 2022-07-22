package com.github.santosleijon.frugalfennecbackend.accounts.domain.projections

import java.util.*

interface AccountProjectionRepository {
    fun save(accountProjection: AccountProjection): AccountProjection
    fun findByIdOrNull(id: UUID): AccountProjection?
    fun findByNameOrNull(name: String): AccountProjection?
    fun findAll(): List<AccountProjection>
    fun delete(id: UUID)
}
