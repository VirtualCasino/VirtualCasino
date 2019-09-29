package pl.edu.pollub.virtualcasino.clientservices.client

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.eventstore.EventSourcedMongoRepository

@Component
class EventSourcedMongoClientRepository(private val factory: ClientFactory,
                                        clientServicesBoundedContextMongoTemplate: MongoTemplate,
                                        objectMapper: ObjectMapper
): EventSourcedMongoRepository<Client, ClientId>(clientServicesBoundedContextMongoTemplate, objectMapper), ClientRepository {

    override fun createAggregate(id: ClientId, events: List<DomainEvent>): Client = factory.create(id, events)

}