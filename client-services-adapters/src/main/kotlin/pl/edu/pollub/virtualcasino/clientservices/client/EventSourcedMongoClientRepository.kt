package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager.*
import pl.edu.pollub.virtualcasino.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.eventstore.EventStore

@Component
class EventSourcedMongoClientRepository(private val clientServicesBoundedContextEventStore: EventStore,
                                        private val clientServicesBoundedContextEventSerializer: EventSerializer,
                                        private val factory: ClientFactory
): ClientRepository {
    
    override fun add(aggregate: Client): Boolean {
        dirtyChecking(aggregate)
        return true
    }

    override fun find(aggregateId: ClientId): Client? {
        val events = clientServicesBoundedContextEventStore.getEventsOfAggregate(aggregateId.value)
                ?.events
                ?.map { clientServicesBoundedContextEventSerializer.deserialize(it) }
                ?: return null
        val aggregate = factory.create(aggregateId, events)
        aggregate.markChangesAsCommitted()
        dirtyChecking(aggregate)
        return aggregate
    }

    override fun clear() {
        clientServicesBoundedContextEventStore.clear()
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
        val serializedPendingEvents = pendingEvents.map { clientServicesBoundedContextEventSerializer.serialize(it) }
        clientServicesBoundedContextEventStore.saveEvents(aggregate.id().value, serializedPendingEvents)
        aggregate.markChangesAsCommitted()
    }
}