package pl.edu.pollub.virtualcasino.clientservices.domain.client.events

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import java.time.Instant
import java.util.UUID.randomUUID

data class TokensCountIncreased(
        val id: TokensCountIncreasedId = TokensCountIncreasedId(),
        val clientId: ClientId,
        val tokens: Tokens,
        val occurredAt: Instant = Instant.now()
): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateUuid(): String = clientId.value

    companion object {
        const val TYPE = "client.tokensCountIncreased"
    }
}

data class TokensCountIncreasedId(val value: String = randomUUID().toString())