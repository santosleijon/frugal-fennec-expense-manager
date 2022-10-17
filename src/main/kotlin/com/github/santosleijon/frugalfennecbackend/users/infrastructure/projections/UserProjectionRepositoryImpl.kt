package com.github.santosleijon.frugalfennecbackend.users.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjection
import com.github.santosleijon.frugalfennecbackend.users.domain.projections.UserProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class UserProjectionRepositoryImpl @Autowired constructor(
    private val userProjectionsDAO: UserProjectionsDAO,
) : UserProjectionRepository {

    override fun save(userProjection: UserProjection): UserProjection {
        userProjectionsDAO.upsert(userProjection)

        return userProjection
    }

    override fun findByEmail(email: String): UserProjection? {
        return userProjectionsDAO.findByEmail(email)
    }

    override fun findById(id: UUID): UserProjection? {
        return userProjectionsDAO.findById(id)
    }
}
