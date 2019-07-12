package pl.edu.pollub.virtualcasino.clientservices.domain

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.TableReserved
import java.time.Instant

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(JsonSubTypes.Type(name = TableReserved.TYPE, value = TableReserved::class))
interface DomainEvent {
    fun type(): String
    fun occurredAt(): Instant
    fun aggregateUuid(): String
}