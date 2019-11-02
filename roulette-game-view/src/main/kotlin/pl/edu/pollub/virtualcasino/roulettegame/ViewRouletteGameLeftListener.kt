package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft

@Component
class ViewRouletteGameLeftListener(
        private val repository: RouletteGameViewRepository,
        private val notifier: RouletteGameNotifier
): DomainEventListener<RouletteGameLeft> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteGameLeft)
    }

    private fun reactTo(event: RouletteGameLeft) {
        val gameView = repository.find(event.aggregateId()) ?: return
        gameView.playersViews.removeIf { it.playerViewId == event.playerId.value }
        repository.save(gameView)
        notifier.notifyThat(event)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteGameLeft

}