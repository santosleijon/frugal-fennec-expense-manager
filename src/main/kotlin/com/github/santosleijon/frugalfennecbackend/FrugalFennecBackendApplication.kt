package com.github.santosleijon.frugalfennecbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod

@SpringBootApplication
class FrugalFennecBackendApplication {
	@Bean
	fun webMvcConfigurer(): WebMvcConfigurer {
		return object : WebMvcConfigurer {
			override fun addCorsMappings(registry: CorsRegistry) {
				registry.addMapping("/**")
					.allowedOrigins("https://localhost:8080")
					.allowCredentials(true)
					.allowedMethods(
						HttpMethod.GET.name,
						HttpMethod.HEAD.name,
						HttpMethod.OPTIONS.name,
						HttpMethod.POST.name,
						HttpMethod.PUT.name,
						HttpMethod.PATCH.name,
						HttpMethod.DELETE.name,
					)
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<FrugalFennecBackendApplication>(*args)
}
