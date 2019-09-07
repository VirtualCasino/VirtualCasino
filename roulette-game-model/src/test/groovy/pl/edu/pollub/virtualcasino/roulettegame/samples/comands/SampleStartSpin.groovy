package pl.edu.pollub.virtualcasino.roulettegame.samples.comands

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.commands.StartSpin

import java.time.Instant

import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId

class SampleStartSpin {

    static StartSpin sampleStartSpin(customProperties = [:]) {
        def properties = [
                gameId: sampleRouletteGameId(),
                bettingTimeEnd: samplePointInTime()
        ] + customProperties
        return new StartSpin(
                properties.gameId as RouletteGameId,
                properties.bettingTimeEnd as Instant
        )
    }

}
