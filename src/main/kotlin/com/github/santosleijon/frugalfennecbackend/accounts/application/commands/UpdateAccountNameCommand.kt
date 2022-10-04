package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.common.errors.UnauthorizedOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateAccountNameCommand @Autowired constructor(
    private val accountRepository: AccountRepository,
) : Command<UpdateAccountNameCommand.Input, Account> {

    data class Input(
        val id: UUID,
        val newName: String,
        val userId: UUID,
    )

    override fun execute(input: Input): Account {
        val id = input.id
        val userId = input.userId

        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        if (account.userId != userId) {
            throw UnauthorizedOperation(this::class, userId)
        }

        account.setName(input.newName, userId)

        return accountRepository.save(account)
            ?: throw AccountNotFoundError(id)
    }
}
