package pl.edu.pollub.virtualcasino.roulettegame.samples

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayer
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId

class SampleRoulettePlayer {

    static RoulettePlayer sampleRoulettePlayer(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId()
        ] + customProperties
        return new RoulettePlayer(properties.clientId as ClientId)
    }

    static RoulettePlayerId samleRoulettePlayerId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new RoulettePlayerId(properties.value as UUID)
    }

}
