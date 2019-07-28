package pl.edu.pollub.virtualcasino.clientservices.domain.table.events

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.time.Instant
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

    override fun aggregateUuid(): String = tableId.value

    companion object {
        const val TYPE = "table.pokerTableReserved"
    }
}

data class PokerTableReservedId(val value: String = randomUUID().toString())