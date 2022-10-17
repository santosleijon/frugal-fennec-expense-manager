package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateAccountCommand @Autowired constructor(
    private val accountRepository: AccountRepository,
    private val accountProjectionRepository: AccountProjectionRepository,
) : Command<CreateAccountCommand.Input, Account> {

    data class Input(
        val id: UUID,
        val name: String,
        val userId: UUID,
    )

    override fun execute(input: Input): Account {
        val id = input.id
        val name = input.name
        val userId = input.userId

        val accountProjection = accountProjectionRepository.findByNameAndUserIdOrNull(name, userId)

        if (accountProjection != null) {
            val account = accountRepository.findByIdOrNull(accountProjection.id)
                ?: throw AccountNotFoundError(id)

            account.undelete(userId)

            return accountRepository.save(account)
                ?: throw AccountNotFoundError(id)
        }

        val newAccount = Account(id, name, userId)

        return accountRepository.save(newAccount)
            ?: throw AccountNotFoundError(id)
    }
}
