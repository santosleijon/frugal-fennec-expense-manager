package com.github.santosleijon.frugalfennecbackend.infrastructure

import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@Suppress("unused")
class AccountRepositoryImpl @Autowired constructor(
    private val eventStore: EventStore,
): AccountRepository {
    override fun save(account: Account): Account? {
        account.pendingEvents.forEach {
            eventStore.append(it)
        }

        return findByIdOrNull(account.id)
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
}
