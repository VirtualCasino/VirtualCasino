package pl.edu.pollub.virtualcasino.clientservices.domain.client.events

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import java.time.Instant
import java.util.UUID.randomUUID

data class ClientCreated(
        val id: UserCreatedId = UserCreatedId(),
        val clientId: ClientId,
        val occurredAt: Instant = Instant.now()
): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateUuid(): String = clientId.value

    companion object {
        const val TYPE = "client.clientCreated"
    }
}



data class UserCreatedId(val value: String = randomUUID().toString())