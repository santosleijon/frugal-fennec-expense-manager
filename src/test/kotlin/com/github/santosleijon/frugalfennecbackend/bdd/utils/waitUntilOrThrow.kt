package com.github.santosleijon.frugalfennecbackend.bdd.utils

suspend fun waitUntilOrThrow(conditionalBlock: () -> Boolean) {
    var timeLeftMillis = 5000L
    val iterationDelayMillis = 200L

    while (timeLeftMillis > 0) {
        if (conditionalBlock()) {
            return
        }

        waitFor(iterationDelayMillis)

        timeLeftMillis -= iterationDelayMillis
    }

    error("Timed out waiting for conditional block")
}
