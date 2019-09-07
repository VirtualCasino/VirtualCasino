package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

class AnySpinNotStartedYet(private val gameId: RouletteGameId,
                           private val playerId: RoulettePlayerId): DomainObjectNotExist(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("playerId", playerId.value.toString())
        ),
        "Player with id: ${playerId.value} can't place bet because any spin isn't started yet") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.anySpinNotStartedYet"
    }
}