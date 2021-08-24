package com.github.santosleijon.frugalfennecbackend.domain.accounts.commands

import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountRepository
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Expense
import com.github.santosleijon.frugalfennecbackend.domain.accounts.errors.AccountNotFoundError
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Component
class AddExpenseCommand {
    @Autowired
    lateinit var accountRepository: AccountRepository

    fun handle(
        id: UUID,
        date: Instant,
        description: String,
        amount: BigDecimal,
    ): Account {
        val account = accountRepository.findByIdOrNull(id)
            ?: throw AccountNotFoundError(id)

        val expense = Expense(id, date, description, amount)

        account.addExpense(expense)

        return accountRepository.save(account)
    }
}