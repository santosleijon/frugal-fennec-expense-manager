package com.github.santosleijon.frugalfennecbackend.bdd

import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import java.io.File

class TestDbContainer {
    companion object {
        private const val DOCKER_COMPOSE_FILEPATH = "./docker-compose.yml"
        private const val DB_DOCKER_SERVICE_NAME = "db"
        private const val DB_EXPOSED_PORT = 5432
        private const val DB_NAME = "frugal_fennec"
        private const val DB_USER = "frugal_fennec"
        private const val DB_PASSWORD = "frugal_fennec"

        @Container
        private val DB_CONTAINER: DockerComposeContainer<*> = DockerComposeContainer<Nothing>(File(
            DOCKER_COMPOSE_FILEPATH
        ))
            .withExposedService(
                DB_DOCKER_SERVICE_NAME,
                DB_EXPOSED_PORT,
                Wait.forLogMessage(".*PostgreSQL init process complete; ready for start up.*\\n", 1)
            )

        init {
            DB_CONTAINER.start()
        }
    }

    val dbUrl: String
        get() = "jdbc:postgresql://" +
                DB_CONTAINER.getServiceHost(DB_DOCKER_SERVICE_NAME, DB_EXPOSED_PORT) +
                ":" +
                DB_CONTAINER.getServicePort(DB_DOCKER_SERVICE_NAME, DB_EXPOSED_PORT) +
                "/$DB_NAME"

    val dbUsername: String
        get() = DB_USER

    val dbPassword: String
        get() = DB_PASSWORD
}
