package com.github.santosleijon.frugalfennecbackend.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjection
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjectionRepository
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
