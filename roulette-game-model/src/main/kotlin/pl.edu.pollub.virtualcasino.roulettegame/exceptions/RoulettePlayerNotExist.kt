package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

class RoulettePlayerNotExist(val gameId: RouletteGameId, val playerId: RoulettePlayerId): DomainObjectNotExist(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("playerId", playerId.value.toString())
        ),
        "Roulette game with id: ${gameId.value} doesn't have player with id: ${playerId.value}") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.roulettePlayerNotExist"
    }
}