package com.github.santosleijon.frugalfennecbackend.common.cqrs

interface Query<I, O> {
    fun execute(input: I): O
}
