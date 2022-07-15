package com.github.santosleijon.frugalfennecbackend.users.application.api

import com.github.santosleijon.frugalfennecbackend.users.application.commands.StartLoginCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("user")
class UserResource @Autowired constructor(
    private val startCommandHandler: StartLoginCommand,
) {
    @PostMapping("start-login")
    fun startLogin(startLoginInputsDTO: StartLoginInputsDTO) {
        startCommandHandler.handle(startLoginInputsDTO.userEmail)
    }

    data class StartLoginInputsDTO(
        val userEmail: String,
    )
}
