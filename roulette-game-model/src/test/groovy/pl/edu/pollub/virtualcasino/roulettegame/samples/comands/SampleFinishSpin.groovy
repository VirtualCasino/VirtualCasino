package pl.edu.pollub.virtualcasino.roulettegame.samples.comands

import pl.edu.pollub.virtualcasino.roulettegame.NumberField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.commands.FinishSpin

import static pl.edu.pollub.virtualcasino.roulettegame.NumberField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId

class SampleFinishSpin {

    static FinishSpin sampleFinishSpin(customProperties = [:]) {
        def properties = [
                gameId: sampleRouletteGameId(),
                fieldDrawn: NUMBER_1
        ] + customProperties
        return new FinishSpin(
                properties.gameId as RouletteGameId,
                properties.fieldDrawn as NumberField
        )
    }

}
