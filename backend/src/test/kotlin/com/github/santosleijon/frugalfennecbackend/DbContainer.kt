package com.github.santosleijon.frugalfennecbackend

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.ImageFromDockerfile
import java.nio.file.Paths

abstract class DbContainer {
    companion object {
        private val DB_IMAGE = ImageFromDockerfile().withFileFromPath(".", Paths.get("./db/."))

        private var DB_CONTAINER: GenericContainer<*> = (GenericContainer<Nothing>(DB_IMAGE) as GenericContainer<*>)
            .withExposedPorts(5432)
            .waitingFor(
                Wait.forLogMessage(".*PostgreSQL init process complete*\\n", 2)
            )

        init {
            DB_CONTAINER.start()
        }
    }

    protected val dbUrl: String
        get() = "jdbc:postgresql://" + DB_CONTAINER.host + ":" + DB_CONTAINER.firstMappedPort + "/frugal-fennec"
}
