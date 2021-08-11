package com.github.santosleijon.frugalfennecbackend.domain.accounts

import com.github.santosleijon.frugalfennecbackend.domain.EventStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AccountRepository {
    @Autowired
    lateinit var eventStore: EventStore

    fun save(account: Account): Account {
        account.pendingEvents.forEach {
            eventStore.append(it)
        }

        return account
    }

    fun findByIdOrNull(id: UUID): Account? {
        val eventStream = eventStore.loadStream(id)

        if (eventStream.events.isEmpty()) {
            return null
        }

        val account = Account.loadFrom(eventStream.events, id, eventStream.version)

        if (account.deleted) {
            return null
        }

        return account
    }

    fun findAll(): Set<Account> {
        TODO()
    }
}