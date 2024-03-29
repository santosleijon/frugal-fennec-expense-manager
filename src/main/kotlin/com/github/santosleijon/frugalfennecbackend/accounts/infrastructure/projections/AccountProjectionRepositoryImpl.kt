package com.github.santosleijon.frugalfennecbackend.accounts.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@Suppress("unused")
class AccountProjectionRepositoryImpl @Autowired constructor(
    val accountProjectionsDAO: AccountProjectionsDAO,
) : AccountProjectionRepository {
    override fun save(accountProjection: AccountProjection): AccountProjection {
        accountProjectionsDAO.upsert(accountProjection)

        return accountProjection
    }

    override fun findByIdOrNull(accountId: UUID): AccountProjection? {
        return accountProjectionsDAO.findById(accountId)
    }

    override fun findByIdAndUserIdOrNull(accountId: UUID, userId: UUID): AccountProjection? {
        return accountProjectionsDAO.findByIdAndUserId(accountId, userId)
    }

    override fun findByNameOrNull(name: String): AccountProjection? {
        return accountProjectionsDAO.findByName(name)
    }

    override fun findByNameAndUserIdOrNull(name: String, userId: UUID): AccountProjection? {
        return accountProjectionsDAO.findByNameAndUserId(name, userId)
    }

    override fun findByUserId(userId: UUID): List<AccountProjection> {
        return accountProjectionsDAO.findByUserId(userId)
    }

    override fun delete(id: UUID) {
        accountProjectionsDAO.delete(id)
    }
}
