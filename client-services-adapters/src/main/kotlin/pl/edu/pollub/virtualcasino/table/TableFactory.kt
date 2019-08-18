package pl.edu.pollub.virtualcasino.table

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.client.ClientRepository

@Component
class TableFactory(private val context: ApplicationContext) {

    fun create(aggregateId: TableId = TableId(), events: List<DomainEvent> = emptyList()): Table {
        val clientRepository = context.getBean(ClientRepository::class.java)
        return Table(aggregateId, clientRepository, events.toMutableList())
    }

}