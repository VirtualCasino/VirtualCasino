package pl.edu.pollub.virtualcasino.clientservices.client.events

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Nick
import java.time.Instant
import java.util.*
import java.util.UUID.*

data class ClientRegistered(
        val id: ClientRegisteredId = ClientRegisteredId(),
        val clientId: ClientId,
        val nick: Nick,
        val occurredAt: Instant = Instant.now()
): DomainEvent {

    override fun type(): String = TYPE

    override fun occurredAt(): Instant = occurredAt

    override fun aggregateId(): UUID = clientId.value

    companion object {
        const val TYPE = "casinoServices.client.clientRegistered"
    }

}

data class ClientRegisteredId(val value: UUID = randomUUID())