package com.github.santosleijon.frugalfennecbackend.accounts.application.api

import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.application.commands.*
import com.github.santosleijon.frugalfennecbackend.accounts.application.queries.GetAccountQuery
import com.github.santosleijon.frugalfennecbackend.accounts.application.queries.GetAllAccountsQuery
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.Instant

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RequestMapping("account")
class AccountResource @Autowired constructor(
    private val createAccountCommand: CreateAccountCommand,
    private val getAllAccounts: GetAllAccountsQuery,
    private val getAccountQuery: GetAccountQuery,
    private val updateAccountNameCommand: UpdateAccountNameCommand,
    private val deleteAccountCommand: DeleteAccountCommand,
    private val addExpenseCommand: AddExpenseCommand,
    private val deleteExpenseCommand: DeleteExpenseCommand,
) {
    @PostMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
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
    fun getAll(): List<AccountProjection> {
        return getAllAccounts.handle()
    }

    @RequestMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@PathVariable(value = "id") id: UUID): AccountProjection {
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
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
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
            expenseInputsDTO.date.toUTCInstant(),
            expenseInputsDTO.description,
            expenseInputsDTO.amount
        )
    }

    data class ExpenseInputsDTO(
        val date: String,
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
            expenseInputsDTO.date.toUTCInstant(),
            expenseInputsDTO.description,
            expenseInputsDTO.amount,
        )
    }
}

fun String.toUTCInstant(): Instant {
    val datePattern = Regex("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])\$")
    val tenDigitValue = this.substring(0, 10)

    if (!(tenDigitValue matches datePattern)) {
        throw IllegalArgumentException("String must be valid date formatted as yyyy-MM-dd")
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse("$tenDigitValue 00:00:00", formatter).toInstant(ZoneOffset.UTC)
}