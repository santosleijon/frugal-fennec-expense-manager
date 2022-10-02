package com.github.santosleijon.frugalfennecbackend.accounts.domain.projections

import java.util.*

interface AccountProjectionRepository {
    fun save(accountProjection: AccountProjection): AccountProjection
    fun findByIdOrNull(accountId: UUID): AccountProjection?
    fun findByIdAndUserIdOrNull(accountId: UUID, userId: UUID): AccountProjection?
    fun findByNameOrNull(name: String): AccountProjection?
    fun findByNameAndUserIdOrNull(name: String, userId: UUID): AccountProjection?
    fun findByUserId(userId: UUID): List<AccountProjection>
    fun delete(id: UUID)
}
