package pl.edu.pollub.virtualcasino.clientservices.domain.table.commands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens

data class ReserveTable(val clientId: ClientId, val gameType: GameType, val initialBidingRate: Tokens = Tokens())

enum class GameType {
    ROULETTE,
    POKER
}