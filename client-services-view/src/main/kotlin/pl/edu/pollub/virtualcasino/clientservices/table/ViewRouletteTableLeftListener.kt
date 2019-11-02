package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft

@Component
class ViewRouletteTableLeftListener(private val repository: TableViewRepository) : DomainEventListener<RouletteGameLeft> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteGameLeft)
    }

    private fun reactTo(event: RouletteGameLeft) {
        val tableView = repository.find(event.aggregateId()) ?: return
        tableView.playersIds.removeIf{ it == event.playerId.value.toString() }
        repository.save(tableView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteGameLeft
}