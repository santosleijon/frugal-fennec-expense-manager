package com.github.santosleijon.frugalfennecbackend.accounts.commands

import com.github.santosleijon.frugalfennecbackend.accounts.Account
import com.github.santosleijon.frugalfennecbackend.accounts.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateAccountCommand {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(
        id: UUID,
        name: String,
    ): Account {
        val newAccount = Account(id, name)
        return accountRepository.save(newAccount)
    }
}