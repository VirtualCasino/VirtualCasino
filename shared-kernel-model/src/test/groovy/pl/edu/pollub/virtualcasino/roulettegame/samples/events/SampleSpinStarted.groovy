package pl.edu.pollub.virtualcasino.roulettegame.samples.events

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinStarted
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinStartedId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId

class SampleSpinStarted {

    static SpinStarted sampleSpinStarted(customProperties = [:]) {
        def properties = [
                id: sampleSpinStartedId(),
                gameId: sampleRouletteGameId(),
                bettingTimeEnd: samplePointInTime(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new SpinStarted(
                properties.id as SpinStartedId,
                properties.gameId as RouletteGameId,
                properties.bettingTimeEnd as Instant,
                properties.occurredAt as Instant
        )
    }

    static SpinStartedId sampleSpinStartedId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new SpinStartedId(properties.value as UUID)
    }

}
