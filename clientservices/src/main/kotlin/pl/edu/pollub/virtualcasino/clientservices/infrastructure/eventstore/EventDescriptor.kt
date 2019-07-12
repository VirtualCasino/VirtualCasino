package pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore

import java.time.Instant
import java.util.UUID.randomUUID

data class EventDescriptor(
        val id: String = randomUUID().toString(),
        val body: String,
        val occurredAt: Instant = Instant.now(),
        val type: String,
        val aggregateId: String
)