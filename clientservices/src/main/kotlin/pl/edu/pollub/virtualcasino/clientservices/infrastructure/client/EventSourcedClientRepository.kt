package pl.edu.pollub.virtualcasino.clientservices.infrastructure.client

import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager.*
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Client
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventStore

@Component
class EventSourcedClientRepository(
        private val eventStore: EventStore,
        private val eventSerializer: EventSerializer,
        private val clientFactory: ClientFactory
): ClientRepository {
    
    override fun add(aggregate: Client): Boolean {
        flushChanges(aggregate)
        dirtyChecking(aggregate)
        return true
    }

    override fun find(aggregateId: ClientId): Client? {
        val events = eventStore.getEventsOfAggregate(aggregateId.value)
                ?.events
                ?.map { eventSerializer.deserialize(it) }
                ?: return null
        val client = clientFactory.create(aggregateId, events)
        client.markChangesAsCommitted()
        dirtyChecking(client)
        return client
    }

    override fun clear() {
        eventStore.clear()
    }

    private fun dirtyChecking(aggregate: Client) {
        if(isSynchronizationActive())
        registerSynchronization(
                object : TransactionSynchronizationAdapter() {

                    override fun beforeCommit(readOnly: Boolean) {
                        if (aggregate.getUncommittedChanges().isNotEmpty()) {
                            flushChanges(aggregate)
                        }
                    }

                }
        )
    }

    private fun flushChanges(aggregate: Client) {
        val pendingEvents = aggregate.getUncommittedChanges()
        val serializedPendingEvents = pendingEvents.map { eventSerializer.serialize(it) }
        eventStore.saveEvents(aggregate.id.value, serializedPendingEvents)
        aggregate.markChangesAsCommitted()
    }
}