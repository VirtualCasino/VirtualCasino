package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.client.events.ClientRegistered

@Component
class ViewClientRegisteredListener(private val repository: ClientViewRepository): DomainEventListener<ClientRegistered> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as ClientRegistered)
    }

    private fun reactTo(event: ClientRegistered) {
        repository.save(ClientView(
                clientViewId = event.aggregateId(),
                nick = event.nick.value,
                tokensCount = event.initialTokens.count
        ))
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is ClientRegistered

}