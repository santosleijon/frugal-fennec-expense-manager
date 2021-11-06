package com.github.santosleijon.frugalfennecbackend.accounts

import com.github.santosleijon.frugalfennecbackend.accounts.commands.*
import com.github.santosleijon.frugalfennecbackend.accounts.queries.GetAccountQuery
import com.github.santosleijon.frugalfennecbackend.accounts.queries.GetAllAccountQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RequestMapping("account")
class AccountResource @Autowired constructor(
    private val createAccountCommand: CreateAccountCommand,
    private val getAllAccounts: GetAllAccountQuery,
    private val getAccountQuery: GetAccountQuery,
    private val updateAccountNameCommand: UpdateAccountNameCommand,
    private val deleteAccountCommand: DeleteAccountCommand,
    private val addExpenseCommand: AddExpenseCommand,
    private val deleteExpenseCommand: DeleteExpenseCommand,
) {
    data class CreateAccountCommandInputsDTO (
        val name: String,
   )

    @PostMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody(required = true) createAccountCommandInputsDTO: CreateAccountCommandInputsDTO): Account {
        return createAccountCommand.handle(
            id = UUID.randomUUID(),
            name = createAccountCommandInputsDTO.name,
        )
    }

    @GetMapping
    fun getAll(): List<Account> {
        return getAllAccounts.handle()
    }

    @RequestMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable(value="id") id: UUID): Account {
        return getAccountQuery.handle(id)
    }

    @PatchMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateName(@PathVariable(value="id") id: UUID, @RequestParam("newName") newName: String): Account? {
        return updateAccountNameCommand.handle(id, newName)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable(value="id") id: UUID) {
        return deleteAccountCommand.handle(id)
    }

    @PostMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addExpense(
        @PathVariable("id") id: UUID,
        @RequestParam("date") date: Instant,
        @RequestParam("description") description: String,
        @RequestParam("amount") amount: BigDecimal,
    ): Account? {
        return addExpenseCommand.handle(id, date, description, amount)
    }

    @DeleteMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteExpense(
        @PathVariable("id") id: UUID,
        @RequestParam("date") date: Instant,
        @RequestParam("description") description: String,
        @RequestParam("amount") amount: BigDecimal,
    ): Account? {
        return deleteExpenseCommand.handle(id, date, description, amount)
    }
}