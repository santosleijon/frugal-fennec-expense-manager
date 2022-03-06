package com.github.santosleijon.frugalfennecbackend.bdd.utils

import kotlinx.coroutines.delay

suspend fun waitFor(millis: Long) {
    var timeLeftMillis = millis
    val iterationDelayMillis = 200L

    while (timeLeftMillis > 0) {
        delay(iterationDelayMillis)
        timeLeftMillis -= iterationDelayMillis
    }
}