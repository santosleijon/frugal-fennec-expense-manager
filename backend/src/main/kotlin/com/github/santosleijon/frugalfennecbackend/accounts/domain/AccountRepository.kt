package com.github.santosleijon.frugalfennecbackend.accounts.domain

import java.util.*

interface AccountRepository {
    fun save(account: Account): Account
    fun findByIdOrNull(id: UUID): Account?
    fun findAll(): List<Account>
}