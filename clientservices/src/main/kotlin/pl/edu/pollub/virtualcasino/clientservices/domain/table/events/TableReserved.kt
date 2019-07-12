package pl.edu.pollub.virtualcasino.clientservices.domain.table.events

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.time.Instant
import java.util.UUID.randomUUID

data class TableReserved(
        val id: TableReservedId = TableReservedId(),
        val tableId: TableId,
        val clientId: ClientId,
        val occurredAt: Instant = Instant.now()

): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateUuid(): String = clientId.value

    companion object {
        const val TYPE = "table.tableReserved"
    }
}

data class TableReservedId(val value: String = randomUUID().toString())