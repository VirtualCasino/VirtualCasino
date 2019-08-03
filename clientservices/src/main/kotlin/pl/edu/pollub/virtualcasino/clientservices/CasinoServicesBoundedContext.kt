package pl.edu.pollub.virtualcasino.clientservices

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source
import org.springframework.integration.config.EnablePublisher
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.mongo.MongoConfigurationProperties
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.publisher.EventsPublishingProperties

@SpringBootApplication
@EnableConfigurationProperties(
        MongoConfigurationProperties::class,
        EventsPublishingProperties::class
)
@EnableTransactionManagement
@EnableScheduling
@EnablePublisher
@EnableBinding(Source::class)
class CasinoServicesBoundedContext {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CasinoServicesBoundedContext::class.java, *args)
        }
    }

}
