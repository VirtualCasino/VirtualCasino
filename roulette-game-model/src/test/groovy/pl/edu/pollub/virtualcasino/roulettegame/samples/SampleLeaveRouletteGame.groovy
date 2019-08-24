package pl.edu.pollub.virtualcasino.roulettegame.samples

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import pl.edu.pollub.virtualcasino.roulettegame.commands.LeaveRouletteGame

import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class SampleLeaveRouletteGame {

    static LeaveRouletteGame sampleLeaveRouletteGame(customProperties = [:]) {
        def properties = [
                playerId: sampleRoulettePlayerId(),
                gameId: sampleRouletteGameId()
        ] + customProperties
        return new LeaveRouletteGame(
                properties.playerId as RoulettePlayerId,
                properties.gameId as RouletteGameId
        )
    }

}
