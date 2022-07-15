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
class AddExpenseCommand @Autowired constructor(
    private val accountRepository: AccountRepository,
) {
    fun handle(
        id: UUID,
        date: Instant,
        description: String,
        amount: BigDecimal,
    ): Account {
        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        val expense = Expense(date, description, amount)

        account.addExpense(expense)

        return accountRepository.save(account)
            ?: throw AccountNotFoundError(id)
    }
}