package com.github.santosleijon.frugalfennecbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FrugalFennecBackendApplication {
	@Bean
	fun webMvcConfigurer(): WebMvcConfigurer {
		return object : WebMvcConfigurer {
			override fun addCorsMappings(registry: CorsRegistry) {
				registry.addMapping("/**")
					.allowedOrigins("http://localhost:8080")
					.allowCredentials(true)
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<FrugalFennecBackendApplication>(*args)
}
