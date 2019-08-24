package pl.edu.pollub.virtualcasino.clientservices.table.samples.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class JoinedTable(
        val id: JoinedTableId = JoinedTableId(),
        val tableId: TableId,
        val clientId: ClientId,
        val clientTokens: Tokens,
        val occurredAt: Instant = Instant.now()
) : DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = tableId.value

    companion object {
        const val TYPE = "casinoServices.table.joinedTable"
    }
}

data class JoinedTableId(val value: UUID = randomUUID())