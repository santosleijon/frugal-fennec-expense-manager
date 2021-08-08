package com.github.santosleijon.frugalfennecbackend.domain.accounts.commands

import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateAccountName {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(
        id: UUID,
        newName: String,
    ): Account? {
        val account = accountRepository.findById(id)
            ?: return null

        account.setName(newName)

        return accountRepository.save(account)
    }
}