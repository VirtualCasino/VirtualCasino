package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.SpinState.IN_PROGRESS
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinStarted

@Component
class ViewRouletteGameSpinStartedListener(private val repository: RouletteGameViewRepository): DomainEventListener<SpinStarted> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as SpinStarted)
    }

    private fun reactTo(event: SpinStarted) {
        val gameView = repository.find(event.aggregateId()) ?: return
        gameView.spinState = IN_PROGRESS
        repository.save(gameView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is SpinStarted

}