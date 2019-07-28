package pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent

@Component
class EventSerializer {

    private val objectMapper: ObjectMapper = ObjectMapper()

    init {
        objectMapper.registerModule(KotlinModule())
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.disable(WRITE_DATES_AS_TIMESTAMPS)
    }

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