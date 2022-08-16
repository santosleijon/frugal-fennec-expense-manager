package com.github.santosleijon.frugalfennecbackend.users.domain.projections

import java.util.UUID

interface UserSessionProjectionRepository {
    fun save(userSessionProjection: UserSessionProjection): UserSessionProjection
    fun findByToken(token: String): UserSessionProjection?
    fun findByUserId(userId: UUID): List<UserSessionProjection>
}
