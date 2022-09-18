package com.github.santosleijon.frugalfennecbackend.users.domain.projections

import java.util.*

interface UserProjectionRepository {
    fun save(userProjection: UserProjection): UserProjection
    fun findByEmail(email: String): UserProjection?
    fun findById(id: UUID): UserProjection?
}
