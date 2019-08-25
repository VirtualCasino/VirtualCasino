package pl.edu.pollub.virtualcasino.clientservices.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import pl.edu.pollub.virtualcasino.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.eventstore.EventStore

@Configuration
class ClientServicesBoundedContextEventStoreConfig {

    @Bean
    fun clientServicesBoundedContextEventStore(clientServicesBoundedContextMongoTemplate: MongoTemplate): EventStore {
        return EventStore(clientServicesBoundedContextMongoTemplate)
    }

    @Bean
    fun clientServicesBoundedContextEventSerializer(): EventSerializer = EventSerializer()
}