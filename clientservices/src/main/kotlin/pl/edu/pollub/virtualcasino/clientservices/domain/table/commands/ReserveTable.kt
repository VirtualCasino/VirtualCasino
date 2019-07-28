package pl.edu.pollub.virtualcasino.clientservices.domain.table.commands

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "gameType")
@JsonSubTypes(
        JsonSubTypes.Type(name = ReserveRouletteTable.GAME_TYPE, value = ReserveRouletteTable::class),
        JsonSubTypes.Type(name = ReservePokerTable.GAME_TYPE, value = ReservePokerTable::class)
)
sealed class ReserveTable {

    abstract fun clientId(): ClientId

    abstract fun gameType(): String
}

data class ReserveRouletteTable(val clientId: ClientId): ReserveTable() {

    override fun clientId(): ClientId = clientId

    override fun gameType(): String = GAME_TYPE

    companion object {
        const val GAME_TYPE = "Roulette"
    }
}

data class ReservePokerTable(val clientId: ClientId, val initialBidingRate: Tokens = Tokens()): ReserveTable() {

    override fun clientId(): ClientId = clientId

    override fun gameType(): String = GAME_TYPE

    companion object {
        const val GAME_TYPE = "Poker"
    }
}