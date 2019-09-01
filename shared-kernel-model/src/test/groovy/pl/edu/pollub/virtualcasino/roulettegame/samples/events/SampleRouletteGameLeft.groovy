package pl.edu.pollub.virtualcasino.roulettegame.samples.events

import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeftId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class SampleRouletteGameLeft {

    static RouletteGameLeft sampleRouletteGameLeft(customProperties = [:]) {
        def properties = [
                id: sampleRouletteGameLeftId(),
                gameId: sampleRouletteGameId(),
                playerId: sampleRoulettePlayerId(),
                playerTokens: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new RouletteGameLeft(
                properties.id as RouletteGameLeftId,
                properties.gameId as RouletteGameId,
                properties.playerId as RoulettePlayerId,
                properties.playerTokens as Tokens,
                properties.occurredAt as Instant
        )
    }

    static RouletteGameLeftId sampleRouletteGameLeftId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new RouletteGameLeftId(properties.value as UUID)
    }

}
