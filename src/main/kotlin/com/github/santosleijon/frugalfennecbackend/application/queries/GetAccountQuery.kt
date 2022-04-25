package com.github.santosleijon.frugalfennecbackend.application.queries

import com.github.santosleijon.frugalfennecbackend.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjection
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjectionRepository
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