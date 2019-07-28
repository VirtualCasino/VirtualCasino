package pl.edu.pollub.virtualcasino.clientservices.infrastructure.mongo

import com.mongodb.MongoClient
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.MongoTransactionManager
import com.github.mongobee.Mongobee

@Configuration
@Profile("default")
class MongoDbConfig {

    @Bean
    fun casinoServicesWriteTemplate(casinoServicesWriteDbTemplateFactory: MongoDbFactory): MongoTemplate {
        return MongoTemplate(casinoServicesWriteDbTemplateFactory)
    }

    @Bean
    fun casinoServicesWriteDbTemplateFactory(casinoServicesWriteDbClient: MongoClient, properties: MongoConfigurationProperties): MongoDbFactory {
        val casinoServicesWriteDb = properties.casinoServicesWriteDb
        return SimpleMongoDbFactory(casinoServicesWriteDbClient, casinoServicesWriteDb.database)
    }

    @Bean
    fun casinoServicesWriteDbClient(properties: MongoConfigurationProperties): MongoClient {
        val casinoServicesWriteDb = properties.casinoServicesWriteDb
        return MongoClient(casinoServicesWriteDb.host, casinoServicesWriteDb.port)
    }

    @Bean
    fun transactionManager(casinoServicesWriteDbTemplateFactory: MongoDbFactory): MongoTransactionManager {
        return MongoTransactionManager(casinoServicesWriteDbTemplateFactory)
    }


}

@Configuration
class MongoBee {

    @Bean
    fun mongobee(properties: MongoConfigurationProperties): Mongobee {
        val casinoServicesWriteDb = properties.casinoServicesWriteDb
        val dbUri = "mongodb://${casinoServicesWriteDb.host}:${casinoServicesWriteDb.port}/${casinoServicesWriteDb.database}"
        val runner = Mongobee(dbUri)
        runner.setDbName(casinoServicesWriteDb.database)
        runner.setChangeLogsScanPackage(DatabaseChangelog::class.java.`package`.name)
        return runner
    }

}

@ConfigurationProperties(prefix = "mongodb")
class MongoConfigurationProperties {

    var casinoServicesWriteDb: MongoProperties = MongoProperties()

}