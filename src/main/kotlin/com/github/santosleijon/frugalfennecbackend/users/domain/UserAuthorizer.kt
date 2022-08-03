package com.github.santosleijon.frugalfennecbackend.users.domain

import org.springframework.stereotype.Component
import java.util.*

@Component
class UserAuthorizer {
    private val tempUserId = UUID.fromString("70a9bd8e-2aec-4623-9191-f3cdc1f8748f")

    fun getUserIdFromSessionToken(sessionToken: String?): UUID {
        if (sessionToken == null){
            // TODO: Remove
            return tempUserId
        }

        // TODO: Check if session exists in user_sessions table

        return tempUserId
    }
}