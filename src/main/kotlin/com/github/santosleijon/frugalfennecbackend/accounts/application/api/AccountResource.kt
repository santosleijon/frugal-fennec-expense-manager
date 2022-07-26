package com.github.santosleijon.frugalfennecbackend.accounts.application.api

import com.github.santosleijon.frugalfennecbackend.accounts.application.api.utils.toUTCInstant
import com.github.santosleijon.frugalfennecbackend.accounts.application.commands.*
import com.github.santosleijon.frugalfennecbackend.accounts.application.queries.GetAccountQuery
import com.github.santosleijon.frugalfennecbackend.accounts.application.queries.GetAllAccountsQuery
import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.projections.AccountProjection
import com.github.santosleijon.frugalfennecbackend.users.domain.UserAuthorizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("account")
class AccountResource @Autowired constructor(
    private val userAuthorizer: UserAuthorizer,
    private val createAccountCommand: CreateAccountCommand,
    private val getAllAccounts: GetAllAccountsQuery,
    private val getAccountQuery: GetAccountQuery,
    private val updateAccountNameCommand: UpdateAccountNameCommand,
    private val deleteAccountCommand: DeleteAccountCommand,
    private val addExpenseCommand: AddExpenseCommand,
    private val deleteExpenseCommand: DeleteExpenseCommand,
) {
    @PostMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody(required = true) createAccountInputsDTO: CreateAccountInputsDTO,
        @RequestHeader("Authorization") authorizationHeader: String?, // TODO: Make non-nullable
    ): Account {
        val userId = userAuthorizer.getUserIdFromAuthorizationHeader(authorizationHeader)

        return createAccountCommand.handle(
            id = UUID.randomUUID(),
            name = createAccountInputsDTO.name,
            userId = userId,
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
    @Suppress("unused")
    fun updateName(
        @PathVariable(value = "id") id: UUID,
        @RequestBody(required = true) updateAccountNameInputsDTO: UpdateAccountNameInputsDTO,
        @RequestHeader("Authorization") authorizationHeader: String?, // TODO: Make non-nullable
    ): Account? {
        val userId = userAuthorizer.getUserIdFromAuthorizationHeader(authorizationHeader)

        return updateAccountNameCommand.handle(id, updateAccountNameInputsDTO.newName, userId)
    }

    data class UpdateAccountNameInputsDTO(
        val newName: String,
    )

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable(value = "id") id: UUID,
        @RequestHeader("Authorization") authorizationHeader: String?, // TODO: Make non-nullable
    ) {
        val userId = userAuthorizer.getUserIdFromAuthorizationHeader(authorizationHeader)

        return deleteAccountCommand.handle(id, userId)
    }

    @PostMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Suppress("unused")
    fun addExpense(
        @PathVariable("id") id: UUID,
        @RequestBody(required = true) expenseInputsDTO: ExpenseInputsDTO,
        @RequestHeader("Authorization") authorizationHeader: String?, // TODO: Make non-nullable
    ): Account? {
        val userId = userAuthorizer.getUserIdFromAuthorizationHeader(authorizationHeader)

        return addExpenseCommand.handle(
            id,
            expenseInputsDTO.date.toUTCInstant(),
            expenseInputsDTO.description,
            expenseInputsDTO.amount,
            userId,
        )
    }

    data class ExpenseInputsDTO(
        val date: String,
        val description: String,
        val amount: BigDecimal,
    )

    @DeleteMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Suppress("unused")
    fun deleteExpense(
        @PathVariable("id") id: UUID,
        @RequestBody(required = true) expenseInputsDTO: ExpenseInputsDTO,
        @RequestHeader("Authorization") authorizationHeader: String?, // TODO: Make non-nullable
    ): Account? {
        val userId = userAuthorizer.getUserIdFromAuthorizationHeader(authorizationHeader)

        return deleteExpenseCommand.handle(
            id,
            expenseInputsDTO.date.toUTCInstant(),
            expenseInputsDTO.description,
            expenseInputsDTO.amount,
            userId,
        )
    }
}
