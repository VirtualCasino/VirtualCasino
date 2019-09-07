package pl.edu.pollub.virtualcasino.roulettegame.commands

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import java.time.Instant

data class StartSpin(val gameId: RouletteGameId, val bettingTimeEnd: Instant)