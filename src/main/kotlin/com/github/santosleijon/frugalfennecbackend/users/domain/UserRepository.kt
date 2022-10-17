package com.github.santosleijon.frugalfennecbackend.users.domain

import java.util.UUID

interface UserRepository {
    fun save(user: User): User?
    fun findById(id: UUID): User?
}
