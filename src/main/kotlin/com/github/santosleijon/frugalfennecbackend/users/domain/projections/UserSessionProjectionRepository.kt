package com.github.santosleijon.frugalfennecbackend.users.domain.projections

interface UserSessionProjectionRepository {
    fun save(userSessionProjection: UserSessionProjection): UserSessionProjection
    fun findByToken(token: String): UserSessionProjection?
}
