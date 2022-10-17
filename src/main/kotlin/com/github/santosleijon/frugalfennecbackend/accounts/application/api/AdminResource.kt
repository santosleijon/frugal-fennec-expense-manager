package com.github.santosleijon.frugalfennecbackend.accounts.application.api

import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Expense
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@RestController
@RequestMapping("admin")
@Suppress("unused")
class AdminResource @Autowired constructor(
    private val accountRepository: AccountRepository,
) {
    @PostMapping("insert-sample-data/{userId}")
    fun insertSampleData(@PathVariable(value = "userId") userId: UUID) {
        val accountNames = listOf(
            "Housing",
            "Transportation",
            "Food",
            "Utilities",
            "Health",
            "Insurance",
            "Interest rates and debt payments",
            "Personal care",
            "Entertainment",
            "Miscellaneous",
        )

        val accounts = accountNames.map {
            Account(
                id = UUID.randomUUID(),
                name = it,
                userId = userId,
            )
        }

        accounts.forEach { account ->
            val expensesCount = ThreadLocalRandom.current().nextInt(5, 15)

            repeat(expensesCount) {
                val expenseAmount = ThreadLocalRandom.current().nextDouble(10.00, 99.99)

                val expenseDateDaysAdjustment = ThreadLocalRandom.current().nextLong(0, 10)
                val expenseDate = Instant.now().minus(expenseDateDaysAdjustment, ChronoUnit.DAYS)

                val expense = Expense(
                    date = expenseDate,
                    description = "A sample expense",
                    amount = BigDecimal.valueOf(expenseAmount)
                )

                account.addExpense(expense, userId)
            }

            accountRepository.save(account)
        }
    }
}
