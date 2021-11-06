package com.github.santosleijon.frugalfennecbackend.accounts.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException
import java.util.*

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Failed to find account")
class AccountNotFoundError (val id: UUID) : RuntimeException("Failed to find account with ID = '$id'")