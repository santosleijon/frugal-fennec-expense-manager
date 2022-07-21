package com.github.santosleijon.frugalfennecbackend.users.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class SessionTokens {

    private val secretKey: String = System.getenv("SESSION_TOKEN_SECRET_KEY")
    private val issuer = "Frugal Fennec Expense Manager"

    private val algorithm = Algorithm.HMAC256(secretKey)

    fun createToken(userEmail: String): String {
        val issuedDate = Instant.now()
        val expirationDate = Instant.now().plus(Duration.ofDays(7))

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(userEmail)
                .withIssuedAt(issuedDate)
                .withExpiresAt(expirationDate)
                .sign(algorithm)
    }

    fun verifyToken(token: String) {
        val verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .build()

        verifier.verify(token)
    }
}
