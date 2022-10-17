package com.github.santosleijon.frugalfennecbackend.users.domain.emailverification

import java.util.regex.Pattern

fun isValidEmail(email: String): Boolean {
    // Definition from RFC 5322 (https://www.baeldung.com/java-email-validation-regex)
    val regexPattern = "^[a-zA-Z\\d_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+$"

    return Pattern.compile(regexPattern)
        .matcher(email)
        .matches()
}