package com.github.santosleijon.frugalfennecbackend.common.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException
import java.util.*
import kotlin.reflect.KClass

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Unauthorized operation")
class UnauthorizedOperation(operation: KClass<out Any>, userId: UUID) :
    RuntimeException("Operation '${operation.simpleName}' is unauthorized for user with ID '${userId}'")
