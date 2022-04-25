package com.github.santosleijon.frugalfennecbackend.application.commands

import com.github.santosleijon.frugalfennecbackend.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountRepository
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
