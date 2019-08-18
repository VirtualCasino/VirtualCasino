package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.client.ClientRepository

@Component
class SpringContextBasedTableFactory(private val context: ApplicationContext): TableFactory {

    override fun create(aggregateId: TableId, events: List<DomainEvent>): Table {
        val clientRepository = context.getBean(ClientRepository::class.java)
        val eventPublisher = context.getBean(TableEventPublisher::class.java)
        return Table(aggregateId, events.toMutableList(), clientRepository, eventPublisher)
    }

}