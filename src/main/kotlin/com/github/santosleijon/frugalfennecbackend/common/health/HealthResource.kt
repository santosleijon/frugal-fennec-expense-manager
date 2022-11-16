package com.github.santosleijon.frugalfennecbackend.common.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("health")
class HealthResource {

    @GetMapping
    fun getHealthStatus(): String {
        return "ok"
    }
}
