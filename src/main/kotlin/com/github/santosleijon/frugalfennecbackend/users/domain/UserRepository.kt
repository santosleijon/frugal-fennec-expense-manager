package com.github.santosleijon.frugalfennecbackend.users.domain

import java.util.UUID

interface UserRepository {
    fun save(user: User)
    fun getById(id: UUID): User?
    fun getByEmail(email: String): User?
}
