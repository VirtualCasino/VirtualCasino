package pl.edu.pollub.virtualcasino.roulettegame.samples

import pl.edu.pollub.virtualcasino.FakedClock
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGame
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameEventPublisher
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGamePublisher

import java.time.Clock

import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId

class SampleRouletteGame {

    static RouletteGame sampleRouletteGame(customProperties = [:]) {
        def properties = [
                id: sampleRouletteGameId(),
                changes: [],
                eventPublisher: new FakedRouletteGamePublisher(),
                clock: new FakedClock(samplePointInTime())
        ] + customProperties
        return new RouletteGame(
                properties.id as RouletteGameId,
                properties.changes as List,
                properties.eventPublisher as RouletteGameEventPublisher,
                properties.clock as Clock
        )
    }

}
