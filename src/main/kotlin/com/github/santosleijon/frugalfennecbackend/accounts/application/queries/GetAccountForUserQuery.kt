package com.github.santosleijon.frugalfennecbackend.accounts.application.queries

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Query
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetAccountForUserQuery @Autowired constructor(
    private val accountProjectionRepository: AccountProjectionRepository,
) : Query<GetAccountForUserQuery.Input, AccountProjection> {

    data class Input(
        val accountId: UUID,
        val userId: UUID,
    )

    override fun execute(input: Input): AccountProjection {
        return accountProjectionRepository.findByIdAndUserIdOrNull(input.accountId, input.userId)
            ?: throw AccountNotFoundError(input.accountId)
    }
}
