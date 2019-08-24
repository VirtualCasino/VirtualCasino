package pl.edu.pollub.virtualcasino.roulettegame.samples

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGame
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameEventPublisher
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGamePublisher

import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId

class SampleRouletteGame {

    static RouletteGame sampleRouletteGame(customProperties = [:]) {
        def properties = [
                id: sampleRouletteGameId(),
                changes: [],
                eventPublisher: new FakedRouletteGamePublisher()
        ] + customProperties
        return new RouletteGame(
                properties.id as RouletteGameId,
                properties.changes as List,
                properties.eventPublisher as RouletteGameEventPublisher
        )
    }

}
