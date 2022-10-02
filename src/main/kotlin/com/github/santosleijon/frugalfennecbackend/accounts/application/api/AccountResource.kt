package com.github.santosleijon.frugalfennecbackend.accounts.application.api

import com.github.santosleijon.frugalfennecbackend.accounts.application.api.utils.toUTCInstant
import com.github.santosleijon.frugalfennecbackend.accounts.application.commands.*
import com.github.santosleijon.frugalfennecbackend.accounts.application.queries.GetAccountForUserQuery
import com.github.santosleijon.frugalfennecbackend.accounts.application.queries.GetAllAccountsForUserQuery
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
@RequestMapping("account")
class AccountResource @Autowired constructor(
    private val userAuthorizer: UserAuthorizer,
    private val createAccountCommand: CreateAccountCommand,
    private val getAllAccountsForUser: GetAllAccountsForUserQuery,
    private val getAccountForUserQuery: GetAccountForUserQuery,
    private val updateAccountNameCommand: UpdateAccountNameCommand,
    private val deleteAccountCommand: DeleteAccountCommand,
    private val addExpenseCommand: AddExpenseCommand,
    private val deleteExpenseCommand: DeleteExpenseCommand,
) {
    @PostMapping("", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(
        @RequestBody(required = true) createAccountInputsDTO: CreateAccountInputsDTO,
        @CookieValue(value = "sessionToken") sessionToken: String,
    ): Account {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionToken)

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
    fun getAllForUser(
        @CookieValue(value = "sessionToken") sessionToken: String,
    ): List<AccountProjection> {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionToken)

        return getAllAccountsForUser.handle(userId)
    }

    @RequestMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(
        @PathVariable(value = "id") id: UUID,
        @CookieValue(value = "sessionToken") sessionToken: String,
    ): AccountProjection {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionToken)

        return getAccountForUserQuery.handle(id, userId)
    }

    @PatchMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Suppress("unused")
    fun updateName(
        @PathVariable(value = "id") id: UUID,
        @RequestBody(required = true) updateAccountNameInputsDTO: UpdateAccountNameInputsDTO,
        @CookieValue(value = "sessionToken") sessionToken: String,
    ): Account? {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionToken)

        return updateAccountNameCommand.handle(id, updateAccountNameInputsDTO.newName, userId)
    }

    data class UpdateAccountNameInputsDTO(
        val newName: String,
    )

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable(value = "id") id: UUID,
        @CookieValue(value = "sessionToken") sessionToken: String,
    ) {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionToken)

        return deleteAccountCommand.handle(id, userId)
    }

    @PostMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Suppress("unused")
    fun addExpense(
        @PathVariable("id") id: UUID,
        @RequestBody(required = true) expenseInputsDTO: ExpenseInputsDTO,
        @CookieValue(value = "sessionToken") sessionToken: String,
    ): Account? {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionToken)

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
        @CookieValue(value = "sessionToken") sessionToken: String,
    ): Account? {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionToken)

        return deleteExpenseCommand.handle(
            id,
            expenseInputsDTO.date.toUTCInstant(),
            expenseInputsDTO.description,
            expenseInputsDTO.amount,
            userId,
        )
    }
}
