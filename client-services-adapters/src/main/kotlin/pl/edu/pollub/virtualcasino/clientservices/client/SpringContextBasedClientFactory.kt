package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository

@Component
class SpringContextBasedClientFactory(private val context: ApplicationContext): ClientFactory {

    override fun create(aggregateId: ClientId, events: List<DomainEvent>): Client {
        val tableRepository = context.getBean(TableRepository::class.java)
        val eventPublisher = context.getBean(ClientEventPublisher::class.java)
        return Client(aggregateId, events.toMutableList(), tableRepository, eventPublisher)
    }

}