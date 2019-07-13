package pl.edu.pollub.virtualcasino.clientservices.domain.client.events

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import java.time.Instant
import java.util.UUID.randomUUID

data class UserCreated(
        val id: UserCreatedId = UserCreatedId()
): DomainEvent {

    override fun type(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun occurredAt(): Instant {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun aggregateUuid(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}



data class UserCreatedId(val value: String = randomUUID().toString())