package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

class BettingTimeExceeded(private val gameId: RouletteGameId,
                          private val playerId: RoulettePlayerId): DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("playerId", playerId.value.toString())
        ),
        "Player with id: ${playerId.value} can't modify bets because betting time is ended") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.bettingTimeExceeded"
    }
}