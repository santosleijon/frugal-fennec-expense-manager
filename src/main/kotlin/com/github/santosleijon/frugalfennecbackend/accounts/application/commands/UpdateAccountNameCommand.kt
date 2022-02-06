package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateAccountNameCommand @Autowired constructor(
    private val accountRepository: AccountRepository,
) {
    fun handle(
        id: UUID,
        newName: String,
    ): Account {
        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        account.setName(newName)

        return accountRepository.save(account)
            ?: throw AccountNotFoundError(id)
    }
}