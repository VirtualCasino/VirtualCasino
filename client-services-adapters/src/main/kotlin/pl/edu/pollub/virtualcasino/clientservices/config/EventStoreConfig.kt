package pl.edu.pollub.virtualcasino.clientservices.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import pl.edu.pollub.virtualcasino.eventstore.ObjectMapperFactory

@Configuration
class ClientServicesBoundedContextEventStoreConfig {

    @Bean
    @Primary
    fun clientObjectMapper(): ObjectMapper = ObjectMapperFactory().create()

}
