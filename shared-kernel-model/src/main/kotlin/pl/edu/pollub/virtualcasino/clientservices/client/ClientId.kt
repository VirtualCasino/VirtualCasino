package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.EventSourcedAggregateRootId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import java.util.*

data class ClientId(val value: UUID = UUID.randomUUID()): EventSourcedAggregateRootId {

    override fun value(): UUID = value

    constructor(playerId: RoulettePlayerId): this(playerId.value)

}