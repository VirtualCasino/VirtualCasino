package pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore

import org.springframework.stereotype.Component

@Component
class EventStore {

    private val eventStreams = mutableMapOf<String, EventStream>()

    fun saveEvents(aggregateId: String, events: List<EventDescriptor>) {
        val eventStream = getEventsOfAggregate(aggregateId) ?: EventStream(aggregateId = aggregateId)
        eventStream.add(events)
        eventStreams[aggregateId] = eventStream
    }

    fun getEventsOfAggregate(aggregateId: String): EventStream? = eventStreams[aggregateId]

    fun clear() = eventStreams.clear()

}