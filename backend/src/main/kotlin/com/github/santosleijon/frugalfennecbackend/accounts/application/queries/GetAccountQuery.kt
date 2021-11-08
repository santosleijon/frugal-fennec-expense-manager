package com.github.santosleijon.frugalfennecbackend.accounts.application.queries

import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class GetAccountQuery {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(id: UUID): Account {
        return accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)
    }
}