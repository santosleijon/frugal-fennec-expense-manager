package com.github.santosleijon.frugalfennecbackend.infrastructure

import com.github.santosleijon.frugalfennecbackend.domain.accounts.Account
import com.github.santosleijon.frugalfennecbackend.domain.EventServer
import com.github.santosleijon.frugalfennecbackend.domain.EventSubscriber
import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.infrastructure.projections.AccountProjector
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EventServerImpl @Autowired constructor(
    // TODO: Replace this dependency by binding the projector to the event server during startup instead of injecting it here
    accountProjector: AccountProjector,
) : EventServer {
    private val subscriberList: Map<String, List<EventSubscriber>> =
        mapOf(Account.aggregateName to listOf(accountProjector))

    override fun publishEvent(event: DomainEvent) {
        val eventSubscribers = subscriberList[event.aggregateName]
            ?: return

        eventSubscribers.forEach { subscriber ->
            subscriber.handleEvent(event)
        }
    }
}
