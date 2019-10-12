package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.client.events.TokensBought

@Component
class ViewClientBoughtTokensListener(private val repository: ClientViewRepository): DomainEventListener<TokensBought> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as TokensBought)
    }

    private fun reactTo(event: TokensBought) {
        val clientView = repository.find(event.clientId.value) ?: return
        clientView.tokensCount += event.tokens.count
        repository.save(clientView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is TokensBought

}