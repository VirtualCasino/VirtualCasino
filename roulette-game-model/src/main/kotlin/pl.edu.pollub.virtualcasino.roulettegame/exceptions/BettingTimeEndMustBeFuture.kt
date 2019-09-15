package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import java.time.Instant

class BettingTimeEndMustBeFuture(private val gameId: RouletteGameId,
                                 private val bettingTimeEnd: Instant,
                                 private val currentTime: Instant): DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("bettingTimeEnd", bettingTimeEnd.toString()),
                Pair("currentTime", currentTime.toString())
        ),
        "Cannot end start new spin in game with id: ${gameId.value} because previous spin is not finished yet") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.bettingTimeEndMustBeFuture"
    }
}