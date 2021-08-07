package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.accounts.commands.CreateAccountCommand
import com.github.santosleijon.frugalfennecbackend.domain.accounts.queries.GetAccountQuery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
@RequestMapping("/account")
class AccountResource {
    @Autowired
    lateinit var createAccountCommand: CreateAccountCommand

    @Autowired
    lateinit var getAccountQuery: GetAccountQuery

    @PostMapping
    fun create(@RequestParam("name") name: String): Account {
        return createAccountCommand.handle(
            id = UUID.randomUUID(),
            name = name,
        )
    }

    @GetMapping
    fun get(@RequestParam("id") id: UUID): Account? {
        return getAccountQuery.handle(id)
    }
}