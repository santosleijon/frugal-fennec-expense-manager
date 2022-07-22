package com.github.santosleijon.frugalfennecbackend.users.infrastructure

import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSessionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class UserSessionsRepositoryImpl @Autowired constructor(
    private val userSessionsDAO: UserSessionsDAO,
) : UserSessionsRepository {

    override fun save(userSession: UserSession) {
        userSessionsDAO.insert(userSession)
    }

    override fun getByToken(token: String): UserSession? {
        return userSessionsDAO.getByToken(token)
    }
}
