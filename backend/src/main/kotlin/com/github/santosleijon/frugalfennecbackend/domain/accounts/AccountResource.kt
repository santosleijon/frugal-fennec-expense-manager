package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.accounts.commands.AddExpenseCommand
import com.github.santosleijon.frugalfennecbackend.domain.accounts.commands.CreateAccountCommand
import com.github.santosleijon.frugalfennecbackend.domain.accounts.commands.DeleteAccountCommand
import com.github.santosleijon.frugalfennecbackend.domain.accounts.commands.UpdateAccountNameCommand
import com.github.santosleijon.frugalfennecbackend.domain.accounts.queries.GetAccountQuery
import com.github.santosleijon.frugalfennecbackend.domain.accounts.queries.GetAllAccountQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Controller
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RequestMapping("/account")
class AccountResource {
    @Autowired
    lateinit var createAccountCommand: CreateAccountCommand

    @Autowired
    lateinit var getAllAccounts: GetAllAccountQuery

    @Autowired
    lateinit var getAccountQuery: GetAccountQuery

    @Autowired
    lateinit var updateAccountNameCommand: UpdateAccountNameCommand

    @Autowired
    lateinit var deleteAccountCommand: DeleteAccountCommand

    @Autowired
    lateinit var addExpenseCommand: AddExpenseCommand

    @PostMapping
    fun create(@RequestParam("name") name: String): Account {
        return createAccountCommand.handle(
            id = UUID.randomUUID(),
            name = name,
        )
    }

    @RequestMapping("/")
    fun getAll(): List<Account> {
        return getAllAccounts.handle()
    }

    @RequestMapping("/{id}")
    fun get(@PathVariable(value="id") id: UUID): Account {
        return getAccountQuery.handle(id)
    }

    @RequestMapping("/{id}", method=[RequestMethod.PATCH])
    fun updateName(@PathVariable(value="id") id: UUID, @RequestParam("newName") newName: String): Account? {
        return updateAccountNameCommand.handle(id, newName)
    }

    @RequestMapping("/{id}", method=[RequestMethod.DELETE])
    fun delete(@PathVariable(value="id") id: UUID) {
        return deleteAccountCommand.handle(id)
    }

    @RequestMapping("/{id}/expense", method=[RequestMethod.POST])
    fun addExpense(
        @PathVariable("id") id: UUID,
        @RequestParam("date") date: Instant,
        @RequestParam("description") description: String,
        @RequestParam("amount") amount: BigDecimal,
    ): Account? {
        return addExpenseCommand.handle(id, date, description, amount)
    }
}