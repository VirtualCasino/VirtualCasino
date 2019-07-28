package pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID.randomUUID

@Document("event_stream")
data class EventStream(
        @Id val id: String = randomUUID().toString(),
        val aggregateId: String,
        @DBRef val events: MutableList<EventDescriptor> = mutableListOf()
) {
    fun add(events: List<EventDescriptor>) {
        this.events.addAll(events)
    }
}