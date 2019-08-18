package pl.edu.pollub.virtualcasino

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.edu.pollub.virtualcasino.clientservices.client.samples.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.RouletteTableReserved
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(name = PokerTableReserved.TYPE, value = PokerTableReserved::class),
        JsonSubTypes.Type(name = RouletteTableReserved.TYPE, value = RouletteTableReserved::class),
        JsonSubTypes.Type(name = JoinedTable.TYPE, value = JoinedTable::class),
        JsonSubTypes.Type(name = TokensBought.TYPE, value = TokensBought::class)
)
interface DomainEvent {
    fun type(): String
    fun occurredAt(): Instant
    fun aggregateId(): UUID
}