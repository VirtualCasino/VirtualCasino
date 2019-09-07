package pl.edu.pollub.virtualcasino.roulettegame.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import pl.edu.pollub.virtualcasino.roulettegame.commands.PlaceBet

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.roulettegame.RouletteField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class SamplePlaceBet {

    static PlaceBet samplePlaceBet(customProperties = [:]) {
        def properties = [
                gameId: sampleRouletteGameId(),
                playerId: sampleRoulettePlayerId(),
                field: FIELD_1,
                value: sampleTokens()
        ] + customProperties
        return new PlaceBet(
                properties.gameId as RouletteGameId,
                properties.playerId as RoulettePlayerId,
                properties.field as RouletteField,
                properties.value as Tokens
        )
    }
}
