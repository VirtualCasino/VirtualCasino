package pl.edu.pollub.virtualcasino.eventstore

import com.fasterxml.jackson.databind.ObjectMapper
import pl.edu.pollub.virtualcasino.DomainEvent

internal class EventSerializer(private val objectMapper: ObjectMapper) {

    fun serialize(event: DomainEvent): EventDescriptor = runCatching {
        val eventBody = objectMapper.writeValueAsString(event)
        EventDescriptor(
                body = eventBody,
                occurredAt = event.occurredAt(),
                type = event.type(),
                aggregateId = event.aggregateId()
        )
    }.getOrThrow()

    fun deserialize(eventDescriptor: EventDescriptor): DomainEvent = runCatching {
        objectMapper.readValue(eventDescriptor.body, DomainEvent::class.java)
    }.getOrThrow()

}