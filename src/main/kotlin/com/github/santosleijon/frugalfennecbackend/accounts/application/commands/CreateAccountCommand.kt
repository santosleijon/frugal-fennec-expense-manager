package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateAccountCommand @Autowired constructor(
    private val accountRepository: AccountRepository,
    private val accountProjectionRepository: AccountProjectionRepository,
) {
    fun handle(
        id: UUID,
        name: String,
    ): Account {
        val accountProjection = accountProjectionRepository.findByNameOrNull(name)

        if (accountProjection != null) {
            val account = accountRepository.findByIdOrNull(accountProjection.id)
                ?: throw AccountNotFoundError(id)

            account.undelete()

            return accountRepository.save(account)
                ?: throw AccountNotFoundError(id)
        }

        val newAccount = Account(id, name)

        return accountRepository.save(newAccount)
            ?: throw AccountNotFoundError(id)
    }
}