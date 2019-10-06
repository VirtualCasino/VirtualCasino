package pl.edu.pollub.virtualcasino.clientservices.config

import com.github.mongobee.Mongobee
import com.mongodb.MongoClient
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory

@Configuration
@Profile("client-services-view")
class ClientServicesViewMongoDbConfig {

    @Bean
    fun clientServicesViewMongoTemplate(clientServicesViewMongoTemplateFactory: MongoDbFactory): MongoTemplate {
        return MongoTemplate(clientServicesViewMongoTemplateFactory)
    }

    @Bean
    fun clientServicesViewMongoTemplateFactory(clientServicesViewMongoClient: MongoClient, properties: ClientServicesViewMongoConfigurationProperties): MongoDbFactory {
        val casinoServicesViewDb = properties.casinoServicesViewDb
        return SimpleMongoDbFactory(clientServicesViewMongoClient, casinoServicesViewDb.database)
    }

    @Bean
    fun clientServicesViewMongoClient(properties: ClientServicesViewMongoConfigurationProperties): MongoClient {
        val casinoServicesViewDb = properties.casinoServicesViewDb
        return MongoClient(casinoServicesViewDb.host, casinoServicesViewDb.port.toInt())
    }

    @Bean
    fun clientServicesViewTransactionManager(clientServicesViewMongoTemplateFactory: MongoDbFactory): MongoTransactionManager {
        return MongoTransactionManager(clientServicesViewMongoTemplateFactory)
    }

}

@Configuration
class ClientServicesViewMongoBee {

    @Bean
    fun clientServicesViewMongobee(properties: ClientServicesViewMongoConfigurationProperties): Mongobee {
        val casinoServicesViewDb = properties.casinoServicesViewDb
        val dbUri = "mongodb://${casinoServicesViewDb.host}:${casinoServicesViewDb.port}/${casinoServicesViewDb.database}"
        val runner = Mongobee(dbUri)
        runner.setDbName(casinoServicesViewDb.database)
        runner.setChangeLogsScanPackage(ClientServicesViewDatabaseChangelog::class.java.`package`.name)
        return runner
    }

}

@Configuration
@ConfigurationProperties(prefix = "mongodb")
class ClientServicesViewMongoConfigurationProperties {

    var casinoServicesViewDb: MongoProperties = MongoProperties()

}