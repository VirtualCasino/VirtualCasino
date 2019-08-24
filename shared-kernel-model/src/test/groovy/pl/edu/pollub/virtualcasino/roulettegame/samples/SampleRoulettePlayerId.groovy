package pl.edu.pollub.virtualcasino.roulettegame.samples

import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

import static java.util.UUID.randomUUID

class SampleRoulettePlayerId {

    static RoulettePlayerId sampleRoulettePlayerId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new RoulettePlayerId(properties.value as UUID)
    }

}
