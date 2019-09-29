package pl.edu.pollub.virtualcasino.clientservices.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.MongoTemplate
import pl.edu.pollub.virtualcasino.eventstore.ObjectMapperFactory

@Configuration
class ClientServicesBoundedContextEventStoreConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper = ObjectMapperFactory().create()

}