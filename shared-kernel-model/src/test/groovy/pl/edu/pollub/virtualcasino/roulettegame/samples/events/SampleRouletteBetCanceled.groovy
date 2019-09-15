package pl.edu.pollub.virtualcasino.roulettegame.samples.events

import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetCanceled
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetCanceledId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.roulettegame.NumberField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class SampleRouletteBetCanceled {

    static RouletteBetCanceled sampleRouletteBetCanceled(customProperties = [:]) {
        def properties = [
                id: sampleRouletteBetCanceledId(),
                gameId: sampleRouletteGameId(),
                playerId: sampleRoulettePlayerId(),
                field: NUMBER_1,
                occurredAt: samplePointInTime()
        ] + customProperties
        return new RouletteBetCanceled(
                properties.id as RouletteBetCanceledId,
                properties.gameId as RouletteGameId,
                properties.playerId as RoulettePlayerId,
                properties.field as RouletteField,
                properties.occurredAt as Instant
        )
    }

    static RouletteBetCanceledId sampleRouletteBetCanceledId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new RouletteBetCanceledId(properties.value as UUID)
    }

}
