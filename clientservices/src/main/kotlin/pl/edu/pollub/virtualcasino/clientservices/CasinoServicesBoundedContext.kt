package pl.edu.pollub.virtualcasino.clientservices

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.transaction.annotation.EnableTransactionManagement
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.mongo.MongoConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(MongoConfigurationProperties::class)
@EnableTransactionManagement
class CasinoServicesBoundedContext {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CasinoServicesBoundedContext::class.java, *args)
        }
    }

}
