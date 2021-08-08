package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.accounts.commands.CreateAccountCommand
import com.github.santosleijon.frugalfennecbackend.domain.accounts.commands.UpdateAccountName
import com.github.santosleijon.frugalfennecbackend.domain.accounts.queries.GetAccountQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

@Controller
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@RequestMapping("/account")
class AccountResource {
    @Autowired
    lateinit var createAccountCommand: CreateAccountCommand

    @Autowired
    lateinit var getAccountQuery: GetAccountQuery

    @Autowired
    lateinit var updateAccountName: UpdateAccountName

    @PostMapping
    fun create(@RequestParam("name") name: String): Account {
        return createAccountCommand.handle(
            id = UUID.randomUUID(),
            name = name,
        )
    }

    @RequestMapping("/{id}")
    fun get(@PathVariable(value="id") id: UUID): Account? {
        return getAccountQuery.handle(id)
    }

    @RequestMapping("/{id}", method=[RequestMethod.PATCH])
    fun updateName(@PathVariable(value="id") id: UUID, @RequestParam("newName") newName: String): Account? {
        return updateAccountName.handle(id, newName)
    }
}