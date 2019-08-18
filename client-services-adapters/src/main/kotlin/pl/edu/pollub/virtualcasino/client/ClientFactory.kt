package pl.edu.pollub.virtualcasino.client

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.table.TableRepository

@Component
class ClientFactory(private val context: ApplicationContext) {

    fun create(aggregateId: ClientId = ClientId(), events: List<DomainEvent> = emptyList()): Client {
        val tableRepository = context.getBean(TableRepository::class.java)
        return Client(aggregateId, tableRepository, events.toMutableList())
    }

}