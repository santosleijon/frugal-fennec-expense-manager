package com.github.santosleijon.frugalfennecbackend.accounts.commands

import com.github.santosleijon.frugalfennecbackend.accounts.Account
import com.github.santosleijon.frugalfennecbackend.accounts.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.errors.AccountNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateAccountNameCommand {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(
        id: UUID,
        newName: String,
    ): Account {
        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        account.setName(newName)

        return accountRepository.save(account)
    }
}