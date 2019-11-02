package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.events.JoinedTable

@Component
class ViewRouletteGameTableJoinedListener(
        private val repository: RouletteGameViewRepository,
        private val notifier: RouletteGameNotifier
): DomainEventListener<JoinedTable> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as JoinedTable)
    }

    private fun reactTo(event: JoinedTable) {
        val gameView = repository.find(event.aggregateId()) ?: return
        gameView.playersViews.add(
                PlayerView(
                        playerViewId = event.clientId.value,
                        tokensCount = event.clientTokens.count
                )
        )
        repository.save(gameView)
        notifier.notifyThat(event)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is JoinedTable

}