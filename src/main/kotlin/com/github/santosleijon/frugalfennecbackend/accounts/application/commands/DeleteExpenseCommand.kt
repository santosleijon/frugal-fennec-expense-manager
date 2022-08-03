package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Component
class DeleteExpenseCommand @Autowired constructor(
    private val accountRepository: AccountRepository,
) {
    fun handle(
        accountId: UUID,
        date: Instant,
        description: String,
        amount: BigDecimal,
        userId: UUID,
    ): Account {
        // TODO: Authorization

        val account = accountRepository.findByIdOrNull(accountId)
            ?: throw AccountNotFoundError(accountId)

        val expense = Expense(date, description, amount)

        account.deleteExpense(expense, userId)

        return accountRepository.save(account)
            ?: throw AccountNotFoundError(accountId)
    }
}
