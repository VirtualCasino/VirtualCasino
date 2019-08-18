package pl.edu.pollub.virtualcasino.table.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.table.TableId
import java.time.Instant
import java.util.*

data class RouletteTableReserved(
        val id: RouletteTableReservedId = RouletteTableReservedId(),
        val tableId: TableId,
        val clientId: ClientId,
        val occurredAt: Instant = Instant.now()

): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = tableId.value

    companion object {
        const val TYPE = "casinoServices.table.rouletteTableReserved"
    }
}

data class RouletteTableReservedId(val value: UUID = UUID.randomUUID())