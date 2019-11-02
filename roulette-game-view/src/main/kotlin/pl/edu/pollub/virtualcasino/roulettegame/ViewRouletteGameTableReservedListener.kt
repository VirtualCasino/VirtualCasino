package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved

@Component
class ViewRouletteGameTableReservedListener(private val repository: RouletteGameViewRepository): DomainEventListener<RouletteTableReserved> {


    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteTableReserved)
    }

    private fun reactTo(event: RouletteTableReserved) {
        val rouletteGameView = RouletteGameView(
                rouletteGameViewId = event.aggregateId(),
                playersViews = mutableListOf(
                        PlayerView(
                                playerViewId = event.clientId.value,
                                tokensCount = event.clientTokens.count
                        )
                )
        )
        repository.save(rouletteGameView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteTableReserved

}