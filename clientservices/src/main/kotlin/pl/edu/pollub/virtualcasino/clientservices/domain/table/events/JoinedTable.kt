package pl.edu.pollub.virtualcasino.clientservices.domain.table.events

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.time.Instant
import java.util.UUID.randomUUID

data class JoinedTable(
        val id: JoinedTableId = JoinedTableId(),
        val tableId: TableId,
        val clientId: ClientId,
        val occurredAt: Instant = Instant.now()
) : DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateUuid(): String = tableId.value

    companion object {
        const val TYPE = "table.joinedTable"
    }
}

data class JoinedTableId(val value: String = randomUUID().toString())