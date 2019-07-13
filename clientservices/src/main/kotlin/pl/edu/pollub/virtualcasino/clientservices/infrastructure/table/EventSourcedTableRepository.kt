package pl.edu.pollub.virtualcasino.clientservices.infrastructure.table

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.clientservices.domain.table.*
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventStore

@Component
class EventSourcedTableRepository(val eventStore: EventStore,
                                  val eventSerializer: EventSerializer,
                                  val tableFactory: TableFactory
): TableRepository {


    val participantsOfTables = mutableListOf<ParticipantsOfTable>()

    override fun add(aggregate: Table): Boolean {
        val pendingEvents = aggregate.getUncommittedChanges()
        val serializedPendingEvents = pendingEvents.map { eventSerializer.serialize(it) }
        eventStore.saveEvents(aggregate.id.value, serializedPendingEvents)
        participantsOfTables.removeIf{ it.tableId == aggregate.id }
        participantsOfTables.add(aggregate.participantsOfTable())
        aggregate.markChangesAsCommitted()
        return true
    }

    override fun find(id: TableId): Table? {
        val events = eventStore.getEventsOfAggregate(id.value)
                ?.events
                ?.map { eventSerializer.deserialize(it) } ?: return null
        return tableFactory.create(id, events)
    }

    override fun clear() {
        eventStore.clear()
        participantsOfTables.clear()
    }

    override fun containsWithParticipation(participation: Participation): Boolean {
        return participantsOfTables.any { it.participation.contains(participation) }
    }

}