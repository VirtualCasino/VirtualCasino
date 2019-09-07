package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import java.time.Instant

class BettingTimeExceeded(private val gameId: RouletteGameId,
                          private val playerId: RoulettePlayerId,
                          private val bettingTimeEnd: Instant): DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("playerId", playerId.value.toString()),
                Pair("bettingTimeEnd", bettingTimeEnd.toString())
        ),
        "Player with id: ${playerId.value} can't modify bets because betting time ended at: $bettingTimeEnd") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.bettingTimeExceeded"
    }
}