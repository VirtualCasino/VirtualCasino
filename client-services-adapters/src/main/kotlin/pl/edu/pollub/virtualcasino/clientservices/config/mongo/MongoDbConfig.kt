package pl.edu.pollub.virtualcasino.clientservices.config.mongo

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
import org.springframework.stereotype.Component

@Configuration
@Profile("default")
class MongoDbConfig {

    @Bean
    fun mongoTemplate(mongoTemplateFactory: MongoDbFactory): MongoTemplate {
        return MongoTemplate(mongoTemplateFactory)
    }

    @Bean
    fun mongoTemplateFactory(mongoClient: MongoClient, properties: MongoConfigurationProperties): MongoDbFactory {
        val casinoServicesWriteDb = properties.casinoServicesDb
        return SimpleMongoDbFactory(mongoClient, casinoServicesWriteDb.database)
    }

    @Bean
    fun mongoClient(properties: MongoConfigurationProperties): MongoClient {
        val casinoServicesWriteDb = properties.casinoServicesDb
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
        val casinoServicesWriteDb = properties.casinoServicesDb
        val dbUri = "mongodb://${casinoServicesWriteDb.host}:${casinoServicesWriteDb.port}/${casinoServicesWriteDb.database}"
        val runner = Mongobee(dbUri)
        runner.setDbName(casinoServicesWriteDb.database)
        runner.setChangeLogsScanPackage(DatabaseChangelog::class.java.`package`.name)
        return runner
    }

}

@Component
@ConfigurationProperties(prefix = "mongodb")
class MongoConfigurationProperties {

    var casinoServicesDb: MongoProperties = MongoProperties()

}