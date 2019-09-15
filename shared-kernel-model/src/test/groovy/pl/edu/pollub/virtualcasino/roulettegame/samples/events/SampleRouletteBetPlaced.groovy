package pl.edu.pollub.virtualcasino.roulettegame.samples.events

import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetPlaced
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetPlacedId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.roulettegame.NumberField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class SampleRouletteBetPlaced {

    static RouletteBetPlaced sampleRouletteBetPlaced(customProperties = [:]) {
        def properties = [
                id: sampleRouletteBetPlacedId(),
                gameId: sampleRouletteGameId(),
                playerId: sampleRoulettePlayerId(),
                field: NUMBER_1,
                value: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new RouletteBetPlaced(
                properties.id as RouletteBetPlacedId,
                properties.gameId as RouletteGameId,
                properties.playerId as RoulettePlayerId,
                properties.field as RouletteField,
                properties.value as Tokens,
                properties.occurredAt as Instant
        )
    }

    static RouletteBetPlacedId sampleRouletteBetPlacedId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new RouletteBetPlacedId(properties.value as UUID)
    }

}
