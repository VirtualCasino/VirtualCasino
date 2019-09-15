package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId

class CurrentSpinNotFinished(private val gameId: RouletteGameId): DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString())
        ),
        "Cannot end start new spin in game with id: ${gameId.value} because previous spin is not finished yet") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.currentSpinNotFinished"
    }
}