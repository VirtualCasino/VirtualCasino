package pl.edu.pollub.virtualcasino

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.edu.pollub.virtualcasino.clientservices.client.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetCanceled
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetPlaced
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import java.time.Instant
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(name = PokerTableReserved.TYPE, value = PokerTableReserved::class),
        JsonSubTypes.Type(name = RouletteTableReserved.TYPE, value = RouletteTableReserved::class),
        JsonSubTypes.Type(name = JoinedTable.TYPE, value = JoinedTable::class),
        JsonSubTypes.Type(name = TokensBought.TYPE, value = TokensBought::class),
        JsonSubTypes.Type(name = RouletteGameLeft.TYPE, value = RouletteGameLeft::class),
        JsonSubTypes.Type(name = RouletteBetPlaced.TYPE, value = RouletteBetPlaced::class),
        JsonSubTypes.Type(name = RouletteBetCanceled.TYPE, value = RouletteBetCanceled::class)
)
interface DomainEvent {
    fun type(): String
    fun occurredAt(): Instant
    fun aggregateId(): UUID
}