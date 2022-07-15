package com.github.santosleijon.frugalfennecbackend.accounts.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.accounts.domain.*
import com.github.santosleijon.frugalfennecbackend.accounts.domain.events.*
import com.github.santosleijon.frugalfennecbackend.common.DomainEvent
import com.github.santosleijon.frugalfennecbackend.common.eventpubsub.EventServer
import com.github.santosleijon.frugalfennecbackend.common.eventpubsub.EventSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class AccountProjector @Autowired constructor(
    eventServer: EventServer,
    private val accountProjectionRepository: AccountProjectionRepository,
) : EventSubscriber(Account.aggregateName, eventServer) {

    override fun handleEvent(event: DomainEvent) {
        when (event) {
            is AccountCreatedEvent -> {
                val projection = AccountProjection(
                    id = event.aggregateId,
                    name = event.name,
                    expenses = emptyList(),
                    version = event.version,
                )

                accountProjectionRepository.save(projection)
            }
            is AccountDeletedEvent -> {
                accountProjectionRepository.delete(event.aggregateId)
            }
            is AccountNameUpdatedEvent -> {
                val updatedProjection = getAccountProjection(event.aggregateId)?.copy(
                    name = event.newName,
                    version = event.version,
                )
                    ?: return

                accountProjectionRepository.save(updatedProjection)
            }
            is ExpenseAddedEvent -> {
                val previousProjection = getAccountProjection(event.aggregateId)
                    ?: return

                val updatedProjection = previousProjection.copy(
                    expenses = previousProjection.expenses + listOf(event.expense).toSet(),
                    version = event.version,
                )

                accountProjectionRepository.save(updatedProjection)
            }
            is ExpenseDeletedEvent -> {
                val previousProjection = getAccountProjection(event.aggregateId)
                    ?: return

                val updatedProjection = previousProjection.copy(
                    expenses = previousProjection.expenses - listOf(event.expense).toSet(),
                    version = event.version,
                    )

                accountProjectionRepository.save(updatedProjection)
            }
        }
    }

    private fun getAccountProjection(id: UUID): AccountProjection? {
        return accountProjectionRepository.findByIdOrNull(id)
    }
}
