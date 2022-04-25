package com.github.santosleijon.frugalfennecbackend.infrastructure.projections

import com.github.santosleijon.frugalfennecbackend.domain.DomainEvent
import com.github.santosleijon.frugalfennecbackend.domain.EventSubscriber
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjection
import com.github.santosleijon.frugalfennecbackend.domain.accounts.AccountProjectionRepository
import com.github.santosleijon.frugalfennecbackend.domain.accounts.events.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountProjector @Autowired constructor(
    private val accountProjectionRepository: AccountProjectionRepository,
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
