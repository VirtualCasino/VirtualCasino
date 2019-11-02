package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.SpinState.*
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinFinished

@Component
class ViewRouletteGameSpinFinishedListener(private val repository: RouletteGameViewRepository): DomainEventListener<SpinFinished> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as SpinFinished)
    }

    private fun reactTo(event: SpinFinished) {
        val gameView = repository.find(event.aggregateId()) ?: return
        gameView.spinState = FINISHED
        repository.save(gameView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is SpinFinished

}