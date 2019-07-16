package pl.edu.pollub.virtualcasino.clientservices.domain.client

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository

@Component
class ClientFactory(val context: ApplicationContext) {

    fun create(aggregateId: ClientId = ClientId(), events: List<DomainEvent> = emptyList()): Client {
        val tableRepository = context.getBean(TableRepository::class.java)
        return Client(aggregateId, events.toMutableList(), tableRepository)
    }

}