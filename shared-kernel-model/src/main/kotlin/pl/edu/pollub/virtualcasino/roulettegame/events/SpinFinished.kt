package pl.edu.pollub.virtualcasino.roulettegame.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.roulettegame.NumberField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import java.time.Instant
import java.util.*

data class SpinFinished (
        val id: SpinFinishedId = SpinFinishedId(),
        val gameId: RouletteGameId,
        val fieldDrawn: NumberField,
        val occurredAt: Instant = Instant.now()
): DomainEvent {

    var results: MutableMap<UUID, Int> = mutableMapOf()

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = gameId.value

    companion object {
        const val TYPE = "rouletteGame.rouletteGame.spinFinished"
    }
}

data class SpinFinishedId(val value: UUID = UUID.randomUUID())