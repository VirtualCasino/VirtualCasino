package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

class BetValueMustBePositive(private val gameId: RouletteGameId,
                             private val playerId: RoulettePlayerId,
                             private val betValue: Tokens): DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("playerId", playerId.value.toString()),
                Pair("betValue", betValue.count.toString())
        ),
        "Player with id: ${playerId.value} can't place bet with value: ${betValue.count} in game with id: ${gameId.value} because bet value must be positive") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.betValueMustBePositive"
    }
}