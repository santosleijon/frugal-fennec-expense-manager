package com.github.santosleijon.frugalfennecbackend.common.cqrs

interface Command<I, O> {
    fun execute(input: I): O
}
