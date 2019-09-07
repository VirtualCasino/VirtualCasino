package pl.edu.pollub.virtualcasino.roulettegame.commands

import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

data class PlaceBet(val gameId: RouletteGameId, val playerId: RoulettePlayerId, val field: RouletteField, val value: Tokens)