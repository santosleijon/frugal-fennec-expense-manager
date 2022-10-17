package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.common.errors.UnauthorizedOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class DeleteAccountCommand @Autowired constructor(
    private val accountRepository: AccountRepository
) : Command<DeleteAccountCommand.Input, Unit> {

    data class Input(
        val id: UUID,
        val userId: UUID,
    )

    override fun execute(input: Input) {
        val id = input.id
        val userId = input.userId

        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        if (account.userId != userId) {
            throw UnauthorizedOperation(this::class, userId)
        }

        account.delete(userId)

        accountRepository.save(account)
    }
}