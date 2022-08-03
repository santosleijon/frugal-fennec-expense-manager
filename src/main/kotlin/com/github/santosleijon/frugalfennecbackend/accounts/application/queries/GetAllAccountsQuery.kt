package com.github.santosleijon.frugalfennecbackend.accounts.application.queries

import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetAllAccountsQuery @Autowired constructor(
    private val accountProjectionRepository: AccountProjectionRepository,
) {
    fun handle(userId: UUID): List<AccountProjection> {
        // TODO: Authorization
        return accountProjectionRepository.findAll(userId)
    }
}
