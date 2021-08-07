package com.github.santosleijon.frugalfennecbackend.domain.accounts.queries

import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetAccountQuery {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(id: UUID): Account? {
        return accountRepository.findById(id)
    }
}