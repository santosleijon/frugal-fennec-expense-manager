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
    @PostMapping("/", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody(required = true) createAccountInputsDTO: CreateAccountInputsDTO): Account {
        return createAccountCommand.handle(
            id = UUID.randomUUID(),
            name = createAccountInputsDTO.name,
        )
    }

    data class CreateAccountInputsDTO(
        val name: String,
    )

    @GetMapping
    fun getAll(): List<Account> {
        return getAllAccounts.handle()
    }

    @RequestMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable(value = "id") id: UUID): Account {
        return getAccountQuery.handle(id)
    }

    @PatchMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateName(
        @PathVariable(value = "id") id: UUID,
        @RequestBody(required = true) updateAccountNameInputsDTO: UpdateAccountNameInputsDTO,
    ): Account? {
        return updateAccountNameCommand.handle(id, updateAccountNameInputsDTO.newName)
    }

    data class UpdateAccountNameInputsDTO(
        val newName: String,
    )

    @DeleteMapping("{id}")
    fun delete(@PathVariable(value = "id") id: UUID) {
        return deleteAccountCommand.handle(id)
    }

    @PostMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addExpense(
        @PathVariable("id") id: UUID,
        @RequestBody(required = true) expenseInputsDTO: ExpenseInputsDTO,
    ): Account? {
        return addExpenseCommand.handle(
            id,
            expenseInputsDTO.date,
            expenseInputsDTO.description,
            expenseInputsDTO.amount
        )
    }

    data class ExpenseInputsDTO(
        val date: Instant,
        val description: String,
        val amount: BigDecimal,
    )

    @DeleteMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteExpense(
        @PathVariable("id") id: UUID,
        @RequestBody(required = true) expenseInputsDTO: ExpenseInputsDTO,
    ): Account? {
        return deleteExpenseCommand.handle(
            id,
            expenseInputsDTO.date,
            expenseInputsDTO.description,
            expenseInputsDTO.amount,
        )
    }
}