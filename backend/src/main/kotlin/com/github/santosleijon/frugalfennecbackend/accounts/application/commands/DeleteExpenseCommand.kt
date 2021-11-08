package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.Account
import com.github.santosleijon.frugalfennecbackend.accounts.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.Expense
import com.github.santosleijon.frugalfennecbackend.accounts.errors.AccountNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Component
class DeleteExpenseCommand {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(
        accountId: UUID,
        date: Instant,
        description: String,
        amount: BigDecimal,
    ): Account {
        val account = accountRepository.findByIdOrNull(accountId)
            ?: throw AccountNotFoundError(accountId)

        val expense = Expense(accountId, date, description, amount)

        account.deleteExpense(expense)

        return accountRepository.save(account)
    }
}