package pl.edu.pollub.virtualcasino.table.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.Tokens
import pl.edu.pollub.virtualcasino.table.TableId
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class PokerTableReserved(
        val id: PokerTableReservedId = PokerTableReservedId(),
        val tableId: TableId,
        val clientId: ClientId,
        val initialBidingRate: Tokens = Tokens(),
        val occurredAt: Instant = Instant.now()

): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = tableId.value

    companion object {
        const val TYPE = "casinoServices.table.pokerTableReserved"
    }
}

data class PokerTableReservedId(val value: UUID = randomUUID())