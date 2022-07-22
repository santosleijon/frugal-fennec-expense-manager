package com.github.santosleijon.frugalfennecbackend.users.domain

interface UserSessionsRepository {
    fun save(userSession: UserSession)
    fun getByToken(token: String): UserSession?
}
