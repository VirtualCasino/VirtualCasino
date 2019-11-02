package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetCanceled

@Component
class ViewRouletteGameBetCanceledListener(
        private val repository: RouletteGameViewRepository,
        private val notifier: RouletteGameNotifier
): DomainEventListener<RouletteBetCanceled> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteBetCanceled)
    }

    private fun reactTo(event: RouletteBetCanceled) {
        val gameView = repository.find(event.aggregateId()) ?: return
        val playerView = gameView.playersViews.find { it.playerViewId == event.playerId.value } ?: return
        playerView.tokensCount -= event.betValue.count
        playerView.betsViews.removeIf { it.field == event.field.toString() }
        repository.save(gameView)
        notifier.notifyThat(event)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteBetCanceled
}