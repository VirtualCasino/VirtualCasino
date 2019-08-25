package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId

class RouletteGameNotExist(val gameId: RouletteGameId):
        DomainObjectNotExist("Roulette game with id: ${gameId.value} doesn't exist") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("gameId", gameId.value.toString()))

    companion object {
        const val CODE = "rouletteGame.rouletteGame.rouletteGameNotExist"
    }
}