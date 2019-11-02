package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.SpinState.*
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinFinished

@Component
class ViewRouletteGameSpinFinishedListener(
        private val repository: RouletteGameViewRepository,
        private val notifier: RouletteGameNotifier
): DomainEventListener<SpinFinished> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as SpinFinished)
    }

    private fun reactTo(event: SpinFinished) {
        val gameView = repository.find(event.aggregateId()) ?: return
        gameView.spinState = FINISHED
        gameView.playersViews.forEach {
            it.betsViews.clear()
            it.tokensCount = event.results[it.playerViewId] ?: 0
        }
        repository.save(gameView)
        notifier.notifyThat(event)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is SpinFinished

}