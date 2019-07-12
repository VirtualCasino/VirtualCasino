package pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore

import java.util.UUID.randomUUID

data class EventStream(
        val id: String = randomUUID().toString(),
        val aggregateId: String,
        val events: MutableList<EventDescriptor> = mutableListOf()
) {
    fun add(events: List<EventDescriptor>) {
        this.events.addAll(events)
    }
}