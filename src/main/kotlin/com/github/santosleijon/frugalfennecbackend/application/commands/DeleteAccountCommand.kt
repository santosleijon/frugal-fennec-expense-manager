package com.github.santosleijon.frugalfennecbackend.application.commands

import com.github.santosleijon.frugalfennecbackend.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class DeleteAccountCommand @Autowired constructor(
    private val accountRepository: AccountRepository
) {
    fun handle(id: UUID) {
        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        account.delete()

        accountRepository.save(account)
    }
}