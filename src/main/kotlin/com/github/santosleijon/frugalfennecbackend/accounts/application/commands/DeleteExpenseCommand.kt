package com.github.santosleijon.frugalfennecbackend.accounts.application.commands

import com.github.santosleijon.frugalfennecbackend.accounts.application.errors.AccountNotFoundError
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import com.github.santosleijon.frugalfennecbackend.common.cqrs.Command
import com.github.santosleijon.frugalfennecbackend.common.errors.UnauthorizedOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Component
class DeleteExpenseCommand @Autowired constructor(
    private val accountRepository: AccountRepository,
) : Command<DeleteExpenseCommand.Input, Account> {

    data class Input(
        val accountId: UUID,
        val date: Instant,
        val description: String,
        val amount: BigDecimal,
        val userId: UUID,
    )

    override fun execute(input: Input): Account {
        val accountId = input.accountId
        val userId = input.userId

        val account = accountRepository.findByIdOrNull(accountId)
            ?: throw AccountNotFoundError(accountId)

        if (account.userId != userId) {
            throw UnauthorizedOperation(this::class, userId)
        }

        val expense = Expense(input.date, input.description, input.amount)

        account.deleteExpense(expense, userId)

        return accountRepository.save(account)
            ?: throw AccountNotFoundError(accountId)
    }
}
