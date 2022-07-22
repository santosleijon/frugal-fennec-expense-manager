package com.github.santosleijon.frugalfennecbackend.users.domain.projections

interface UserProjectionRepository {
    fun save(userProjection: UserProjection): UserProjection
    fun findByEmail(email: String): UserProjection?
}
