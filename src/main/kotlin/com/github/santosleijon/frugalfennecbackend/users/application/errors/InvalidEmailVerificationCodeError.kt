package com.github.santosleijon.frugalfennecbackend.users.application.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid email verification code")
class InvalidEmailVerificationCodeError (val email: String) : RuntimeException("Invalid email verification code for email '$email'")