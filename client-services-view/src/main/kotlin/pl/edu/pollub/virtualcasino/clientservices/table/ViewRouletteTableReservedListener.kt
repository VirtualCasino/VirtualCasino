package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved

@Component
class ViewRouletteTableReservedListener(private val repository: TableViewRepository) : DomainEventListener<RouletteTableReserved> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteTableReserved)
    }

    private fun reactTo(event: RouletteTableReserved) {
        val tableView = TableView(
                tableViewId = event.aggregateId(),
                firstPlayerNick = event.firstPlayerNick.value,
                playersIds = mutableSetOf(event.clientId.value.toString()),
                maxPlayersCount = event.gameType().maxPlayers,
                gameType = event.gameType().gameName
        )
        repository.save(tableView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteTableReserved

}