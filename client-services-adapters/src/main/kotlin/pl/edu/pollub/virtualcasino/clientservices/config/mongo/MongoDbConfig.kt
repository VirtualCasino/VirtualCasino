package pl.edu.pollub.virtualcasino.clientservices.config.mongo

import com.github.mongobee.Mongobee
import com.mongodb.MongoClient
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory

@Configuration
@Profile("client-services")
class ClientServicesBoundedContextMongoDbConfig {

    @Bean
    fun clientServicesBoundedContextMongoTemplate(clientServicesBoundedContextMongoTemplateFactory: MongoDbFactory): MongoTemplate {
        return MongoTemplate(clientServicesBoundedContextMongoTemplateFactory)
    }

    @Bean
    fun clientServicesBoundedContextMongoTemplateFactory(clientServicesBoundedContextMongoClient: MongoClient, properties: ClientServicesBoundedContextMongoConfigurationProperties): MongoDbFactory {
        val casinoServicesDb = properties.casinoServicesDb
        return SimpleMongoDbFactory(clientServicesBoundedContextMongoClient, casinoServicesDb.database)
    }

    @Bean
    fun clientServicesBoundedContextMongoClient(properties: ClientServicesBoundedContextMongoConfigurationProperties): MongoClient {
        val casinoServicesDb = properties.casinoServicesDb
        return MongoClient(casinoServicesDb.host, casinoServicesDb.port.toInt())
    }

    @Bean
    @Primary
    fun clientServicesBoundedContextTransactionManager(clientServicesBoundedContextMongoTemplateFactory: MongoDbFactory): MongoTransactionManager {
        return MongoTransactionManager(clientServicesBoundedContextMongoTemplateFactory)
    }

}

@Configuration
class ClientServicesBoundedContextMongoBee {

    @Bean
    fun clientServicesBoundedContexMongobee(properties: ClientServicesBoundedContextMongoConfigurationProperties): Mongobee {
        val casinoServicesDb = properties.casinoServicesDb
        val dbUri = "mongodb://${casinoServicesDb.host}:${casinoServicesDb.port}/${casinoServicesDb.database}"
        val runner = Mongobee(dbUri)
        runner.setDbName(casinoServicesDb.database)
        runner.setChangeLogsScanPackage(ClientServicesBoundedContextDatabaseChangelog::class.java.`package`.name)
        return runner
    }

}

@Configuration
@ConfigurationProperties(prefix = "mongodb")
class ClientServicesBoundedContextMongoConfigurationProperties {

    var casinoServicesDb: MongoProperties = MongoProperties()

}
