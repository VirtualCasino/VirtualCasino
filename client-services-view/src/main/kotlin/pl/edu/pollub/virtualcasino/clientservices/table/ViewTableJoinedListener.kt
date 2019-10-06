package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.events.JoinedTable
import java.lang.IllegalStateException

@Component
class ViewTableJoinedListener(private val repository: TableViewRepository): DomainEventListener<JoinedTable> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as JoinedTable)
    }

    private fun reactTo(event: JoinedTable) {
        val tableView = repository.find(event.aggregateId()) ?: throw IllegalStateException("TableView with id: ${event.aggregateId()} not found")
        tableView.playersIds.add(event.clientId.value.toString())
        repository.save(tableView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is JoinedTable

}