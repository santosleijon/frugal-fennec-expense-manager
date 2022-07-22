package com.github.santosleijon.frugalfennecbackend.users.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class UserSessions @Autowired constructor(
    private val userSessionsRepository: UserSessionsRepository,
) {

    private val sessionTokenSecretKey: String = System.getenv("SESSION_TOKEN_SECRET_KEY")
    private val sessionTokenIssuer = "Frugal Fennec Expense Manager"

    private val algorithm = Algorithm.HMAC256(sessionTokenSecretKey)

    fun create(userId: UUID): UserSession {
        val issuedDate = Instant.now()
        val expirationDate = Instant.now().plus(Duration.ofDays(7))

        val token = JWT.create()
                .withIssuer(sessionTokenIssuer)
                .withSubject(userId.toString())
                .withIssuedAt(issuedDate)
                .withExpiresAt(expirationDate)
                .sign(algorithm)

        val userSession = UserSession(
            userId = userId,
            token = token,
            issued = issuedDate,
            validTo = expirationDate,
        )

        userSessionsRepository.save(userSession)

        return userSession
    }

    fun isValid(token: String): Boolean {
        try {
            verifyToken(token)
        } catch (e: JWTVerificationException) {
            return false
        }

        return userSessionsRepository.getByToken(token) != null
    }

    private fun verifyToken(token: String) {
        val verifier = JWT.require(algorithm)
            .withIssuer(sessionTokenIssuer)
            .build()

        verifier.verify(token)
    }
}