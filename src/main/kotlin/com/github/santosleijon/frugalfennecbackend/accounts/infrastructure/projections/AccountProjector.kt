package com.github.santosleijon.frugalfennecbackend.accounts.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjection
import com.github.santosleijon.frugalfennecbackend.accounts.domain.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.accounts.domain.events.*
import com.github.santosleijon.frugalfennecbackend.eventsourcing.DomainEvent
import com.github.santosleijon.frugalfennecbackend.eventsourcing.EventSubscriber
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountProjector @Autowired constructor(
    private val accountProjectionRepository: AccountProjectionRepository
) : EventSubscriber {

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
                    expenses = previousProjection.expenses + listOf(event.expense),
                    version = event.version,
                )

                accountProjectionRepository.save(updatedProjection)
            }
            is ExpenseDeletedEvent -> {
                val previousProjection = getAccountProjection(event.aggregateId)
                    ?: return

                val updatedProjection = previousProjection.copy(
                    expenses = previousProjection.expenses - listOf(event.expense),
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