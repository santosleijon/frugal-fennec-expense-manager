package com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjection
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserSessionProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Suppress("unused")
class UserSessionProjectionRepositoryImpl @Autowired constructor(
    private val userSessionProjectionsDAO: UserSessionProjectionsDAO,
) : UserSessionProjectionRepository {

    override fun save(userSessionProjection: UserSessionProjection): UserSessionProjection {
        userSessionProjectionsDAO.upsert(userSessionProjection)

        return userSessionProjection
    }

    override fun findValidSessionByToken(token: String): UserSessionProjection? {
        return userSessionProjectionsDAO.findValidSessionByToken(token)
    }

    override fun findByUserId(userId: UUID): List<UserSessionProjection> {
        return userSessionProjectionsDAO.findByUserId(userId)
    }
}
