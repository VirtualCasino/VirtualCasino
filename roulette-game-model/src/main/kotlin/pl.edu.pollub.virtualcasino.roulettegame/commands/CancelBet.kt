package pl.edu.pollub.virtualcasino.roulettegame.commands

import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

data class CancelRouletteBet(val gameId: RouletteGameId, val playerId: RoulettePlayerId, val field: RouletteField)