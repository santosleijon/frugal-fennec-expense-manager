package com.github.santosleijon.frugalfennecbackend.accounts.application.queries

import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjectionRepository
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
