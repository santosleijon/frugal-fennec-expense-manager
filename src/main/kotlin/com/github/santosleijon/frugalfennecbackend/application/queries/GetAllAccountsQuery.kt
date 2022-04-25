package com.github.santosleijon.frugalfennecbackend.application.queries

import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjection
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetAllAccountsQuery @Autowired constructor(
    private val accountProjectionRepository: AccountProjectionRepository,
) {
    fun handle(): List<AccountProjection> {
        return accountProjectionRepository.findAll()
    }
}
