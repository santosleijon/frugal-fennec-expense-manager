package com.github.santosleijon.frugalfennecbackend.eventsourcing

import com.github.santosleijon.frugalfennecbackend.accounts.domain.Account
import com.github.santosleijon.frugalfennecbackend.accounts.infrastructure.projections.AccountProjector
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EventServer @Autowired constructor(
    // TODO: Replace this dependency by binding the projector to the event server during startup instead of injecting it here
    accountProjector: AccountProjector
) {
    private val subscriberList: Map<String, List<EventSubscriber>> =
        mapOf(Account.aggregateName to listOf(accountProjector))

    fun publishEvent(event: DomainEvent) {
        val eventSubscribers = subscriberList[event.aggregateName]
            ?: return

        eventSubscribers.forEach { subscriber ->
            subscriber.handleEvent(event)
        }
    }
}
