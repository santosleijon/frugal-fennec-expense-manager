package com.github.santosleijon.frugalfennecbackend.users.application.api

import com.github.santosleijon.frugalfennecbackend.users.application.commands.StartLoginCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:8080"])
@RequestMapping("user")
class UserResource @Autowired constructor(
    private val startCommandHandler: StartLoginCommand,
) {
    @PostMapping("start-login")
    fun startLogin(@RequestBody(required = true) startLoginInputsDTO: StartLoginInputsDTO) {
        startCommandHandler.handle(startLoginInputsDTO.userEmail)
    }

    data class StartLoginInputsDTO(
        val userEmail: String,
    )
}
