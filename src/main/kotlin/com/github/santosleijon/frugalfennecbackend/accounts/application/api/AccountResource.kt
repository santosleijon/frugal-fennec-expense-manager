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
import javax.validation.Valid
import javax.validation.constraints.*

@RestController
@RequestMapping("api/account")
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
        @Valid @RequestBody(required = true) createAccountInputsDTO: CreateAccountInputsDTO,
        @CookieValue(value = "sessionId") sessionId: UUID,
    ): Account {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionId)

        val commandInput = CreateAccountCommand.Input(
            id = UUID.randomUUID(),
            name = createAccountInputsDTO.name,
            userId = userId,
        )

        return createAccountCommand.execute(commandInput)
    }

    data class CreateAccountInputsDTO(
        @field:NotBlank val name: String,
    )

    @GetMapping
    fun getAllForUser(
        @CookieValue(value = "sessionId") sessionId: UUID,
    ): List<AccountProjection> {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionId)

        val queryInput = GetAllAccountsForUserQuery.Input(userId)

        return getAllAccountsForUser.execute(queryInput)
    }

    @RequestMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(
        @PathVariable(value = "id") id: UUID,
        @CookieValue(value = "sessionId") sessionId: UUID,
    ): AccountProjection {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionId)

        val queryInput = GetAccountForUserQuery.Input(id, userId)

        return getAccountForUserQuery.execute(queryInput)
    }

    @PatchMapping("{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Suppress("unused")
    fun updateName(
        @PathVariable(value = "id") id: UUID,
        @Valid @RequestBody(required = true) updateAccountNameInputsDTO: UpdateAccountNameInputsDTO,
        @CookieValue(value = "sessionId") sessionId: UUID,
    ): Account? {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionId)

        val commandInput = UpdateAccountNameCommand.Input(
            id,
            updateAccountNameInputsDTO.newName,
            userId,
        )

        return updateAccountNameCommand.execute(commandInput)
    }

    data class UpdateAccountNameInputsDTO(
        @field:NotBlank val newName: String,
    )

    @DeleteMapping("{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable(value = "id") id: UUID,
        @CookieValue(value = "sessionId") sessionId: UUID,
    ) {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionId)

        val commandInput = DeleteAccountCommand.Input(id, userId)

        return deleteAccountCommand.execute(commandInput)
    }

    @PostMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Suppress("unused")
    fun addExpense(
        @PathVariable("id") id: UUID,
        @Valid @RequestBody(required = true) expenseInputsDTO: ExpenseInputsDTO,
        @CookieValue(value = "sessionId") sessionId: UUID,
    ): Account? {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionId)

        val commandInput = AddExpenseCommand.Input(
            id,
            expenseInputsDTO.date.toUTCInstant(),
            expenseInputsDTO.description,
            expenseInputsDTO.amount,
            userId,
        )

        return addExpenseCommand.execute(commandInput)
    }

    data class ExpenseInputsDTO(
        @field:NotNull
        @field:Pattern(regexp="^([0-9]{4})-([0-9]{2})-([0-9]{2})(?:T([0-9]{2}):([0-9]{2}):([0-9]{2})Z)?$")
        val date: String,

        @field:NotNull
        val description: String,

        @field:NotNull
        @field:DecimalMin(value = "0.00")
        @field:Digits(integer=9, fraction=2)
        val amount: BigDecimal,
    )

    @DeleteMapping("{id}/expense", produces = [MediaType.APPLICATION_JSON_VALUE])
    @Suppress("unused")
    fun deleteExpense(
        @PathVariable("id") id: UUID,
        @Valid @RequestBody(required = true) expenseInputsDTO: ExpenseInputsDTO,
        @CookieValue(value = "sessionId") sessionId: UUID,
    ): Account? {
        val userId = userAuthorizer.validateUserSessionAndGetUserId(sessionId)

        val commandInput = DeleteExpenseCommand.Input(
            id,
            expenseInputsDTO.date.toUTCInstant(),
            expenseInputsDTO.description,
            expenseInputsDTO.amount,
            userId,
        )

        return deleteExpenseCommand.execute(commandInput)
    }
}
