package com.github.santosleijon.frugalfennecbackend.users.application.api

import com.github.santosleijon.frugalfennecbackend.users.application.commands.CompleteLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.application.commands.StartLoginCommand
import com.github.santosleijon.frugalfennecbackend.users.domain.UserSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("user")
class UserResource @Autowired constructor(
    private val startCommandHandler: StartLoginCommand,
    private val completeCommandHandler: CompleteLoginCommand,
) {
    @PostMapping("start-login")
    fun startLogin(@RequestBody(required = true) startLoginInputsDTO: StartLoginInputsDTO) {
        startCommandHandler.handle(startLoginInputsDTO.userEmail)
    }

    @PostMapping("complete-login")
    fun completeLogin(@RequestBody(required = true) completeLoginInputsDTO: CompleteLoginInputsDTO): UserSession {
        return completeCommandHandler.handle(completeLoginInputsDTO.userEmail, completeLoginInputsDTO.verificationCode)
    }

    data class StartLoginInputsDTO(
        val userEmail: String,
    )

    data class CompleteLoginInputsDTO(
        val userEmail: String,
        val verificationCode: String,
    )
}
