package com.github.santosleijon.frugalfennecbackend.domain.accounts

import java.util.*

interface AccountRepository {
    fun save(account: Account): Account?
    fun findByIdOrNull(id: UUID): Account?
}
