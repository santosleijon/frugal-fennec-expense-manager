package com.github.santosleijon.frugalfennecbackend.accounts.infrastructure

import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountRepository
import com.github.santosleijon.frugalfennecbackend.eventsourcing.EventStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@Suppress("unused")
class AccountRepositoryImpl @Autowired constructor(
    private val eventStore: EventStore
): AccountRepository {
    override fun save(account: Account): Account {
        account.pendingEvents.forEach {
            eventStore.append(it)
        }

        return account
    }

    override fun findByIdOrNull(id: UUID): Account? {
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

    override fun findAll(): List<Account> {
        val eventStreams = eventStore.loadStreamsByAggregateName(Account.aggregateName)

        if (eventStreams.isEmpty()) {
            return emptyList()
        }

        return eventStreams.map {
            Account.loadFrom(
                events = it.events,
                id = it.events.first().aggregateId,
                version = it.version
            )
        }.filterNot {
            it.deleted
        }
    }
}