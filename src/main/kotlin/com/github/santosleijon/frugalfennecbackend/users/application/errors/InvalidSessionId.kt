package com.github.santosleijon.frugalfennecbackend.users.application.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Request lacks valid authentication credentials")
class InvalidSessionId(sessionId: UUID) : RuntimeException("Session ID '$sessionId' is invalid")
