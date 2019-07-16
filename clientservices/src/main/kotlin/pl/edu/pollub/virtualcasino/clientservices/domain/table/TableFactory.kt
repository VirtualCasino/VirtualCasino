package pl.edu.pollub.virtualcasino.clientservices.domain.table

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository

@Component
class TableFactory(val context: ApplicationContext) {

    fun create(aggregateId: TableId = TableId(), events: List<DomainEvent> = emptyList()): Table {
        val tableRequirementsFactory = context.getBean(TableRequirementsFactory::class.java)
        val clientRepository = context.getBean(ClientRepository::class.java)
        return Table(aggregateId, events.toMutableList(), tableRequirementsFactory, clientRepository)
    }

}