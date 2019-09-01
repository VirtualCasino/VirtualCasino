package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId

class RouletteGameNotExist(private val gameId: RouletteGameId): DomainObjectNotExist(
        CODE,
        mapOf(Pair("gameId", gameId.value.toString())),
        "Roulette game with id: ${gameId.value} doesn't exist") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.rouletteGameNotExist"
    }
}