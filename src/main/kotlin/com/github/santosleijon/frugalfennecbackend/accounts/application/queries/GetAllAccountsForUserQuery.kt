package com.github.santosleijon.frugalfennecbackend.accounts.application.queries

import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetAllAccountsForUserQuery @Autowired constructor(
    private val accountProjectionRepository: AccountProjectionRepository,
) : Command<GetAllAccountsForUserQuery.Input, List<AccountProjection>> {

    data class Input(
        val userId: UUID,
    )

    override fun execute(input: Input): List<AccountProjection> {
        return accountProjectionRepository.findByUserId(input.userId)
    }
}
