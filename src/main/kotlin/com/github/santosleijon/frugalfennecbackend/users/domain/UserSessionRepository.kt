package com.github.santosleijon.frugalfennecbackend.users.domain

import java.util.*

interface UserSessionRepository {
    fun save(userSession: UserSession): UserSession?
    fun findById(id: UUID): UserSession?
}
