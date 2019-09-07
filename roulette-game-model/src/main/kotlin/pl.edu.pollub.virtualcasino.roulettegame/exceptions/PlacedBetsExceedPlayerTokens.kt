package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

class PlacedBetsExceedPlayerFreeTokens(private val gameId: RouletteGameId,
                                       private val playerId: RoulettePlayerId,
                                       private val betValue: Tokens,
                                       private val playerFreeTokens: Tokens): DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("playerId", playerId.value.toString()),
                Pair("betValue", betValue.count.toString()),
                Pair("playerFreeTokens", playerFreeTokens.count.toString())
        ),
        "Player with id: ${playerId.value} can't place bet with value: ${betValue.count} because it's value exceeds free tokens count: ${playerFreeTokens.count}") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.placedBetsExceedPlayerTokens"
    }
}