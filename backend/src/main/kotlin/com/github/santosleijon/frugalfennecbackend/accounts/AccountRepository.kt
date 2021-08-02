package com.github.santosleijon.frugalfennecbackend.accounts

import com.github.santosleijon.frugalfennecbackend.IEvent
import java.util.*

class AccountRepository {
    var inMemoryStream: MutableMap<Int, List<IEvent>> = HashMap<Int, List<IEvent>>()

    fun getAccount(number: Int): Account {
        val account = Account(number)

        inMemoryStream[number]?.forEach {
            account.addEvent(it)
        }

        return account
    }

    fun saveAccount(account: Account) {
        inMemoryStream[account.number] = account.getEvents()
    }
}