package pl.edu.pollub.virtualcasino.clientservices.client.samples.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

data class TokensBought(
        val id: TokensBoughtId = TokensBoughtId(),
        val clientId: ClientId,
        val tokens: Tokens,
        val occurredAt: Instant = Instant.now()
): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = clientId.value

    companion object {
        const val TYPE = "casinoServices.client.tokensBought"
    }
}

data class TokensBoughtId(val value: UUID = randomUUID())