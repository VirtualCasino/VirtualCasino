package pl.edu.pollub.virtualcasino.roulettegame.samples

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId

import static java.util.UUID.randomUUID

class SampleRouletteGameId {

    static RouletteGameId sampleRouletteGameId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new RouletteGameId(properties.value as UUID)
    }

}
