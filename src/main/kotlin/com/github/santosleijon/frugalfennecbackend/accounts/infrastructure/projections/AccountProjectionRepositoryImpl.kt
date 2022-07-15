package com.github.santosleijon.frugalfennecbackend.accounts.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjectionRepository
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

    override fun findByIdOrNull(id: UUID): AccountProjection? {
        return accountProjectionsDAO.findById(id)
    }

    override fun findByNameOrNull(name: String): AccountProjection? {
        return accountProjectionsDAO.findByName(name)
    }

    override fun findAll(): List<AccountProjection> {
        return accountProjectionsDAO.findAll()
    }

    override fun delete(id: UUID) {
        accountProjectionsDAO.delete(id)
    }
}
