package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager
import pl.edu.pollub.virtualcasino.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.eventstore.EventStore
import pl.edu.pollub.virtualcasino.eventstore.EventStream

@Component
class EventSourcedMongoRouletteGameRepository(val eventStore: EventStore,
                                              val eventSerializer: EventSerializer,
                                              val rouletteGameFactory: RouletteGameFactory,
                                              val mongo: MongoTemplate
): RouletteGameRepository {

    override fun add(aggregate: RouletteGame): Boolean {
        flushChanges(aggregate)
        dirtyChecking(aggregate)
        return true
    }

    override fun find(id: RouletteGameId): RouletteGame? {
        val events = eventStore.getEventsOfAggregate(id.value)
                ?.events
                ?.map { eventSerializer.deserialize(it) } ?: return null
        val aggregate = rouletteGameFactory.create(id, events)
        aggregate.markChangesAsCommitted()
        dirtyChecking(aggregate)
        return aggregate
    }

    override fun clear() {
        eventStore.clear()
    }

    override fun contains(id: RouletteGameId): Boolean {
        val query = Query()
        query.addCriteria(Criteria.where("aggregateId").isEqualTo(id))
        return mongo.exists(query, EventStream::class.java)
    }

    private fun dirtyChecking(aggregate: RouletteGame) {
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

    private fun flushChanges(aggregate: RouletteGame) {
        val pendingEvents = aggregate.getUncommittedChanges()
        val serializedPendingEvents = pendingEvents.map { eventSerializer.serialize(it) }
        eventStore.saveEvents(aggregate.id.value, serializedPendingEvents)
        aggregate.markChangesAsCommitted()
    }
}