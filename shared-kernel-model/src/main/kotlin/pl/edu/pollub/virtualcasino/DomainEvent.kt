package pl.edu.pollub.virtualcasino

import java.time.Instant
import java.util.*

interface DomainEvent {
    fun type(): String
    fun occurredAt(): Instant
    fun aggregateId(): UUID
}