package pl.edu.pollub.virtualcasino.clientservices.domain.client.events

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
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
        const val TYPE = "client.tokensBought"
    }
}

data class TokensBoughtId(val value: UUID = randomUUID())