package pl.edu.pollub.virtualcasino.clientservices.table.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.events.GameType.ROULETTE
import java.time.Instant
import java.util.*

data class RouletteTableReserved(
        val id: RouletteTableReservedId = RouletteTableReservedId(),
        val tableId: TableId,
        val clientId: ClientId,
        val clientTokens: Tokens,
        val occurredAt: Instant = Instant.now()

): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = tableId.value

    fun gameType(): GameType = ROULETTE

    companion object {
        const val TYPE = "casinoServices.table.rouletteTableReserved"
    }
}

data class RouletteTableReservedId(val value: UUID = UUID.randomUUID())
