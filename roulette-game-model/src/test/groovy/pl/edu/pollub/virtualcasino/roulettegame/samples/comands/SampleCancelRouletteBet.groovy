package pl.edu.pollub.virtualcasino.roulettegame.samples.comands

import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import pl.edu.pollub.virtualcasino.roulettegame.commands.CancelRouletteBet

import static pl.edu.pollub.virtualcasino.roulettegame.NumberField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class SampleCancelRouletteBet {

    static CancelRouletteBet sampleCancelRouletteBet(customProperties = [:]) {
        def properties = [
                gameId: sampleRouletteGameId(),
                playerId: sampleRoulettePlayerId(),
                field: NUMBER_1
        ] + customProperties
        return new CancelRouletteBet(
                properties.gameId as RouletteGameId,
                properties.playerId as RoulettePlayerId,
                properties.field as RouletteField
        )
    }

}
