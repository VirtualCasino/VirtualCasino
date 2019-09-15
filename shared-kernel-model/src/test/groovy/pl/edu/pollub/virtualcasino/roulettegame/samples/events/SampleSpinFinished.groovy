package pl.edu.pollub.virtualcasino.roulettegame.samples.events

import pl.edu.pollub.virtualcasino.roulettegame.NumberField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinFinished
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinFinishedId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.roulettegame.NumberField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId

class SampleSpinFinished {

    static SpinFinished sampleSpinFinished(customProperties = [:]) {
        def properties = [
                id: sampleSpinFinishedId(),
                gameId: sampleRouletteGameId(),
                fieldDrawn: NUMBER_1,
                occurredAt: samplePointInTime()
        ] + customProperties
        return new SpinFinished(
                properties.id as SpinFinishedId,
                properties.gameId as RouletteGameId,
                properties.fieldDrawn as NumberField,
                properties.occurredAt as Instant
        )
    }

    static SpinFinishedId sampleSpinFinishedId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new SpinFinishedId(properties.value as UUID)
    }

}
