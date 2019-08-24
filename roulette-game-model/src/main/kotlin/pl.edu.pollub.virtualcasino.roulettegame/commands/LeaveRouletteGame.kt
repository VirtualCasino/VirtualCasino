package pl.edu.pollub.virtualcasino.roulettegame.commands

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

data class LeaveRouletteGame(val playerId: RoulettePlayerId, val gameId: RouletteGameId)