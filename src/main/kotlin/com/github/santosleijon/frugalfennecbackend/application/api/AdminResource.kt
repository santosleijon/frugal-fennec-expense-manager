package com.github.santosleijon.frugalfennecbackend.application.api

import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountRepository
import com.github.santosleijon.frugalfennecbackend.domain.accounts.Expense
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("admin")
class AdminResource @Autowired constructor(
    private val accountRepository: AccountRepository,
) {
    @PostMapping("insert-sample-data")
    fun insertSampleData() {
        val accounts = listOf(
            Account(UUID.randomUUID(), "Housing"),
            Account(UUID.randomUUID(), "Transportation"),
            Account(UUID.randomUUID(), "Food"),
            Account(UUID.randomUUID(), "Utilities"),
            Account(UUID.randomUUID(), "Health"),
            Account(UUID.randomUUID(), "Insurance"),
            Account(UUID.randomUUID(), "Interest rates and debt payments"),
            Account(UUID.randomUUID(), "Personal care"),
            Account(UUID.randomUUID(), "Entertainment"),
            Account(UUID.randomUUID(), "Miscellaneous"),
        )

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

                account.addExpense(expense)
            }

            accountRepository.save(account)
        }
    }
}
