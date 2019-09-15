package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId

class SpinAlreadyFinished(private val gameId: RouletteGameId): DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString())
        ),
        "Spin in game with id: ${gameId.value} is already finished") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.spinAlreadyFinished"
    }
}