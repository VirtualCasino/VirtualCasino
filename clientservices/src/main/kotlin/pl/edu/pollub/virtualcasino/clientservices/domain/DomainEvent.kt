package pl.edu.pollub.virtualcasino.clientservices.domain

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.RouletteTableReserved
import java.time.Instant

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
    fun aggregateUuid(): String
}