package pl.edu.pollub.virtualcasino.roulettegame.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import pl.edu.pollub.virtualcasino.roulettegame.commands.PlaceRouletteBet

import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.roulettegame.RouletteField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class SamplePlaceRouletteBet {

    static PlaceRouletteBet samplePlaceRouletteBet(customProperties = [:]) {
        def properties = [
                gameId: sampleRouletteGameId(),
                playerId: sampleRoulettePlayerId(),
                field: FIELD_1,
                value: sampleTokens()
        ] + customProperties
        return new PlaceRouletteBet(
                properties.gameId as RouletteGameId,
                properties.playerId as RoulettePlayerId,
                properties.field as RouletteField,
                properties.value as Tokens
        )
    }
}
