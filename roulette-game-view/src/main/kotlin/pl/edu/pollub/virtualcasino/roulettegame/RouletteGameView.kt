package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pollub.virtualcasino.roulettegame.SpinState.NOT_STARTED
import java.util.*

@Document("roulette_game_view")
class RouletteGameView(@Id val id: UUID = UUID.randomUUID(),
                       val rouletteGameViewId: UUID,
                       val playersViews: MutableList<PlayerView> = mutableListOf(),
                       var spinState: SpinState = NOT_STARTED)

class PlayerView(val playerViewId: UUID,
                 var tokensCount: Int,
                 val betsViews: MutableList<BetView> = mutableListOf())

class BetView(var field: String,
              var value: Int)

enum class SpinState {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED
}