package pl.edu.pollub.virtualcasino.eventstore

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRootId

abstract class EventSourcedMongoRepository<T : EventSourcedAggregateRoot<I>, I : EventSourcedAggregateRootId>(
        private val mongoTemplate: MongoTemplate,
        objectMapper: ObjectMapper
) {

    private val eventStore = EventStore(mongoTemplate)
    private val eventSerializer = EventSerializer(objectMapper)

    abstract fun createAggregate(id: I, events: List<DomainEvent>): T

    open fun persistProjections(aggregate: T) {}

    fun add(aggregate: T): Boolean {
        flushChanges(aggregate)
        dirtyChecking(aggregate)
        return true
    }

    fun find(id: I): T? {
        val events = eventStore.getEventsOfAggregate(id.value())
                ?.events
                ?.map { eventSerializer.deserialize(it) } ?: return null
        val aggregate = createAggregate(id, events)
        aggregate.markChangesAsCommitted()
        dirtyChecking(aggregate)
        return aggregate
    }

    fun clear() {
        eventStore.clear()
    }

    fun contains(id: I): Boolean {
        val query = Query()
        query.addCriteria(Criteria.where("aggregateId").isEqualTo(id))
        return mongoTemplate.exists(query, EventStream::class.java)
    }

    private fun dirtyChecking(aggregate: T) {
        if(TransactionSynchronizationManager.isSynchronizationActive())
            TransactionSynchronizationManager.registerSynchronization(
                    object : TransactionSynchronizationAdapter() {

                        override fun beforeCommit(readOnly: Boolean) {
                            if (aggregate.getUncommittedChanges().isNotEmpty()) {
                                flushChanges(aggregate)
                            }
                        }

                    }
            )
    }

    private fun flushChanges(aggregate: T) {
        val pendingEvents = aggregate.getUncommittedChanges()
        val serializedPendingEvents = pendingEvents.map { eventSerializer.serialize(it) }
        eventStore.saveEvents(aggregate.id().value(), serializedPendingEvents)
        persistProjections(aggregate)
        aggregate.markChangesAsCommitted()
    }

}