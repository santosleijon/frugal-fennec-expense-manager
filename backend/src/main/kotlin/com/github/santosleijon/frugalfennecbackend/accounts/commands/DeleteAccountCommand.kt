package com.github.santosleijon.frugalfennecbackend.accounts.commands

import com.github.santosleijon.frugalfennecbackend.accounts.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.errors.AccountNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class DeleteAccountCommand {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(id: UUID) {
        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        account.delete()

        accountRepository.save(account)
    }
}