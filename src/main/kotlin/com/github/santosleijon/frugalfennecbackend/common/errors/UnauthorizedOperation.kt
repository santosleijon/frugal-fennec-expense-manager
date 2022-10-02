package com.github.santosleijon.frugalfennecbackend.common.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Unauthorized operation")
class UnauthorizedOperation (operationDescription: String) : RuntimeException("Operation '$operationDescription' is unauthorized for user")
