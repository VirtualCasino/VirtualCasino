package pl.edu.pollub.virtualcasino.roulettegame.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.roulettegame.RouletteField
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class RouletteBetCanceled(
        val id: RouletteBetCanceledId = RouletteBetCanceledId(),
        val gameId: RouletteGameId,
        val playerId: RoulettePlayerId,
        val field: RouletteField,
        val occurredAt: Instant = Instant.now()
): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = gameId.value

    companion object {
        const val TYPE = "rouletteGame.rouletteGame.rouletteBetCanceled"
    }

}

data class RouletteBetCanceledId(val value: UUID = randomUUID())