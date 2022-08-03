package com.github.santosleijon.frugalfennecbackend.accounts.domain.projections

import java.util.*

interface AccountProjectionRepository {
    fun save(accountProjection: AccountProjection): AccountProjection
    fun findByIdOrNull(accountId: UUID): AccountProjection?
    fun findByNameOrNull(name: String, userId: UUID): AccountProjection?
    fun findAll(userId: UUID): List<AccountProjection>
    fun delete(id: UUID)
}
