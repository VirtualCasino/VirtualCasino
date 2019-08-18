package pl.edu.pollub.virtualcasino.table

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager.isSynchronizationActive
import org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization
import pl.edu.pollub.virtualcasino.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.eventstore.EventStore
import pl.edu.pollub.virtualcasino.infrastructure.table.ParticipantsOfTable

@Component
class EventSourcedTableRepository(val eventStore: EventStore,
                                  val eventSerializer: EventSerializer,
                                  val tableFactory: TableFactory,
                                  val mongo: MongoTemplate
): TableRepository {

    override fun add(aggregate: Table): Boolean {
        flushChanges(aggregate)
        dirtyChecking(aggregate)
        return true
    }

    override fun find(id: TableId): Table? {
        val events = eventStore.getEventsOfAggregate(id.value)
                ?.events
                ?.map { eventSerializer.deserialize(it) } ?: return null
        val table = tableFactory.create(id, events)
        table.markChangesAsCommitted()
        dirtyChecking(table)
        return table
    }

    override fun clear() {
        eventStore.clear()
        mongo.remove(Query(), ParticipantsOfTable::class.java)
    }

    override fun containsWithParticipation(participation: Participation): Boolean {
        val query = Query()
        query.addCriteria(Criteria.where("participation.clientId").`is`(participation.clientId))
        val projection = mongo.findOne(query, ParticipantsOfTable::class.java)
        return projection != null
    }

    private fun saveParticipantsProjection(aggregate: Table) {
        val query = Query()
        query.addCriteria(Criteria.where("tableId").isEqualTo(aggregate.id))
        val projection = mongo.findOne(query, ParticipantsOfTable::class.java) ?: ParticipantsOfTable(tableId = aggregate.id)
        projection.participation = aggregate.participation()
        mongo.save(projection)
    }

    private fun dirtyChecking(aggregate: Table) {
        if(isSynchronizationActive())
        registerSynchronization(
                object: TransactionSynchronizationAdapter() {

                    override fun afterCommit() {
                        if(aggregate.getUncommittedChanges().isNotEmpty()) {
                            flushChanges(aggregate)
                        }
                    }

                }
        )
    }

    private fun flushChanges(aggregate: Table) {
        val pendingEvents = aggregate.getUncommittedChanges()
        val serializedPendingEvents = pendingEvents.map { eventSerializer.serialize(it) }
        eventStore.saveEvents(aggregate.id.value, serializedPendingEvents)
        saveParticipantsProjection(aggregate)
        aggregate.markChangesAsCommitted()
    }
}