package com.github.santosleijon.frugalfennecbackend.accounts.application.queries

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetAccountQuery @Autowired constructor(
    private val accountProjectionRepository: AccountProjectionRepository,
) {
    fun handle(id: UUID): AccountProjection {
        return accountProjectionRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)
    }
}