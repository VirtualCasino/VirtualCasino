package pl.edu.pollub.virtualcasino.roulettegame.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import java.time.Instant
import java.util.*

data class SpinStarted (
        val id: SpinStartedId = SpinStartedId(),
        val gameId: RouletteGameId,
        val bettingTimeEnd: Instant,
        val occurredAt: Instant = Instant.now()
        ): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = gameId.value

    companion object {
        const val TYPE = "rouletteGame.rouletteGame.spinStarted"
    }
}

data class SpinStartedId(val value: UUID = UUID.randomUUID())