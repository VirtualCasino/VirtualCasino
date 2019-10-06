package pl.edu.pollub.virtualcasino.clientservices.table.commands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.events.GameType
import pl.edu.pollub.virtualcasino.clientservices.table.events.GameType.POKER
import pl.edu.pollub.virtualcasino.clientservices.table.events.GameType.ROULETTE

sealed class ReserveTable {

    abstract fun clientId(): ClientId

    abstract fun gameType(): GameType
}

data class ReserveRouletteTable(val clientId: ClientId): ReserveTable() {

    override fun clientId(): ClientId = clientId

    override fun gameType(): GameType = ROULETTE

}

data class ReservePokerTable(val clientId: ClientId, val initialBidingRate: Tokens = Tokens()): ReserveTable() {

    override fun clientId(): ClientId = clientId

    override fun gameType(): GameType = POKER

}
