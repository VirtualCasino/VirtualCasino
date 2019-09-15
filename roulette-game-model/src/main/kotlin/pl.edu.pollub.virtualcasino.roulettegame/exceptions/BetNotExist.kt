package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

class BetNotExist(private val gameId: RouletteGameId,
                  private val playerId: RoulettePlayerId,
                  private val field: RouletteField): DomainObjectNotExist(
        CODE,
        mapOf(
                Pair("gameId", gameId.value.toString()),
                Pair("playerId", playerId.value.toString()),
                Pair("field", field.toString())
        ),
        "Player with id: ${playerId.value} can't cancel bet in game with id: ${gameId.value} for field: $field because it hasn't any bet for that field") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.betNotExist"
    }
}