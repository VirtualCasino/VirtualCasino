package pl.edu.pollub.virtualcasino.eventstore

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import java.util.UUID.randomUUID

@Document("event_stream")
data class EventStream(
        @Id val id: UUID = randomUUID(),
        val aggregateId: UUID,
        @DBRef val events: MutableList<EventDescriptor> = mutableListOf()
) {
    fun add(events: List<EventDescriptor>) {
        this.events.addAll(events)
    }
}