package com.github.santosleijon.financelynxbackend.accounts

import com.github.santosleijon.financelynxbackend.IEvent
import com.github.santosleijon.financelynxbackend.accounts.events.AccountCreatedEvent
import com.github.santosleijon.financelynxbackend.accounts.events.AccountCreditedEvent
import com.github.santosleijon.financelynxbackend.accounts.events.AccountDebitedEvent
import java.math.BigDecimal
import java.time.Instant

class Account(
    val number: Int,
    private var name: String? = null,
    var balance: BigDecimal? = null,
    var type: AccountType? = null
) {
    private val events: MutableList<IEvent> = emptyList<IEvent>().toMutableList()

    fun create(name: String, type: AccountType) {
        addEvent(
            AccountCreatedEvent(number, name, type)
        )
    }

    fun debit(amount: BigDecimal, reason: String) {
        if (amount < BigDecimal.ZERO) {
            // TODO: Throw custom exception
            throw Error("Negative amounts cannot be debited")
        }

        if (type == AccountType.CREDIT && balance?.abs()!! < amount) {
            // TODO: Create custom exception
            throw Error("Overdraft is not permitted")
        }

        addEvent(
            AccountDebitedEvent(number, Instant.now(), amount, reason)
        )
    }

    fun credit(amount: BigDecimal, reason: String) {
        if (amount < BigDecimal.ZERO) {
            // TODO: Throw custom exception
            throw Error("Negative amounts cannot be credited")
        }

        if (type == AccountType.DEBIT && balance!! < amount) {
            // TODO: Create custom exception
            throw Error("Overdraft is not permitted")
        }

        addEvent(
            AccountCreditedEvent(number, Instant.now(), amount, reason)
        )
    }

    fun getEvents(): List<IEvent> {
        return events
    }

    fun addEvent(event: IEvent) {
        applyEvent(event)
        events.add(event)
    }

    private fun applyEvent(event: IEvent) {
        when(event) {
            is AccountCreatedEvent -> {
                name = event.name
                balance = BigDecimal.ZERO
                type = event.type
            }
            is AccountDebitedEvent -> {
                balance = balance?.add(event.amount)
            }
            is AccountCreditedEvent -> {
                balance = balance?.subtract(event.amount)
            }
        }
    }
}