package com.github.santosleijon.frugalfennecbackend.accounts.application.queries

import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetAllAccountsQuery {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(): List<Account> {
        return accountRepository.findAll()
    }
}