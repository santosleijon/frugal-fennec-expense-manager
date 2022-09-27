package com.github.santosleijon.frugalfennecbackend.users.application.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Request lacks valid authentication credentials")
class InvalidSessionToken(sessionToken: String) : RuntimeException("Session token '$sessionToken' is invalid")
