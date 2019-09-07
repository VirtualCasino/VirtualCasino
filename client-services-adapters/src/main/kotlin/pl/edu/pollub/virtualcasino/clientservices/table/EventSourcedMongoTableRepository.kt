package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager.isSynchronizationActive
import org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization
import pl.edu.pollub.virtualcasino.clientservices.table.projection.TableParticipants
import pl.edu.pollub.virtualcasino.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.eventstore.EventStore

@Component
class EventSourcedMongoTableRepository(val clientServicesBoundedContextEventStore: EventStore,
                                       val clientServicesBoundedContextEventSerializer: EventSerializer,
                                       val factory: TableFactory,
                                       val clientServicesBoundedContextMongoTemplate: MongoTemplate
): TableRepository {

    override fun add(aggregate: Table): Boolean {
        flushChanges(aggregate)
        dirtyChecking(aggregate)
        return true
    }

    override fun find(id: TableId): Table? {
        val events = clientServicesBoundedContextEventStore.getEventsOfAggregate(id.value)
                ?.events
                ?.map { clientServicesBoundedContextEventSerializer.deserialize(it) } ?: return null
        val aggregate = factory.create(id, events)
        aggregate.markChangesAsCommitted()
        dirtyChecking(aggregate)
        return aggregate
    }

    override fun clear() {
        clientServicesBoundedContextEventStore.clear()
        clientServicesBoundedContextMongoTemplate.remove(Query(), TableParticipants::class.java)
    }

    override fun containsWithParticipation(participation: Participation): Boolean {
        val query = Query()
        query.addCriteria(Criteria.where("participation.clientId").`is`(participation.clientId))
        val projection = clientServicesBoundedContextMongoTemplate.findOne(query, TableParticipants::class.java)
        return projection != null
    }

    private fun saveParticipantsProjection(aggregate: Table) {
        val query = Query()
        query.addCriteria(Criteria.where("tableId").isEqualTo(aggregate.id()))
        val projection = clientServicesBoundedContextMongoTemplate.findOne(query, TableParticipants::class.java) ?: TableParticipants(tableId = aggregate.id())
        projection.participation = aggregate.participation()
        clientServicesBoundedContextMongoTemplate.save(projection)
    }

    private fun dirtyChecking(aggregate: Table) {
        if(isSynchronizationActive())
        registerSynchronization(
                object: TransactionSynchronizationAdapter() {

                    override fun beforeCommit(readOnly: Boolean) {
                        if(aggregate.getUncommittedChanges().isNotEmpty()) {
                            flushChanges(aggregate)
                        }
                    }

                }
        )
    }

    private fun flushChanges(aggregate: Table) {
        val pendingEvents = aggregate.getUncommittedChanges()
        val serializedPendingEvents = pendingEvents.map { clientServicesBoundedContextEventSerializer.serialize(it) }
        clientServicesBoundedContextEventStore.saveEvents(aggregate.id().value, serializedPendingEvents)
        saveParticipantsProjection(aggregate)
        aggregate.markChangesAsCommitted()
    }
}