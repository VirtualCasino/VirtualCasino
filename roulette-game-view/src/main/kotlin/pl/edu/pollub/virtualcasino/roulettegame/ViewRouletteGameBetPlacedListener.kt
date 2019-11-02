package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetPlaced

@Component
class ViewRouletteGameBetPlacedListener(private val repository: RouletteGameViewRepository): DomainEventListener<RouletteBetPlaced> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteBetPlaced)
    }

    private fun reactTo(event: RouletteBetPlaced) {
        val gameView = repository.find(event.aggregateId()) ?: return
        val playerView = gameView.playersViews.find { it.playerViewId == event.playerId.value } ?: return
        playerView.tokensCount -= event.betValue.count
        val betView = playerView.betsViews.find { it.field == event.field.toString() } ?: run {
            val newBet = BetView(event.field.toString(), 0)
            playerView.betsViews.add(newBet)
            newBet
        }
        betView.value = event.betValue.count
        repository.save(gameView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteBetPlaced
}