package pl.edu.pollub.virtualcasino.clientservices.table.commands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens

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