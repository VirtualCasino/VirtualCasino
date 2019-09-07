package pl.edu.pollub.virtualcasino.roulettegame.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import pl.edu.pollub.virtualcasino.roulettegame.RoulettePlayerId
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class RouletteGameLeft(
        val id: RouletteGameLeftId = RouletteGameLeftId(),
        val gameId: RouletteGameId,
        val playerId: RoulettePlayerId,
        val playerTokens: Tokens,
        val occurredAt: Instant = Instant.now()
): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = gameId.value

    companion object {
        const val TYPE = "rouletteGame.rouletteGame.rouletteGameLeft"
    }

}

data class RouletteGameLeftId(val value: UUID = randomUUID())