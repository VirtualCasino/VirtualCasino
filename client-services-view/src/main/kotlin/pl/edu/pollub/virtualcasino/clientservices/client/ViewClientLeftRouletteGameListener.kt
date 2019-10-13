package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft

@Component
class ViewClientLeftRouletteGameListener(private val repository: ClientViewRepository) : DomainEventListener<RouletteGameLeft> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteGameLeft)
    }

    private fun reactTo(event: RouletteGameLeft) {
        val clientView = repository.find(event.playerId.value) ?: return
        clientView.tokensCount = event.playerTokens.count
        repository.save(clientView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteGameLeft
}