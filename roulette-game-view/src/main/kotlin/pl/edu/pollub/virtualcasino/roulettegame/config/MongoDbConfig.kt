package pl.edu.pollub.virtualcasino.roulettegame.config

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
@Profile("roulette-game-view")
class RouletteGameViewMongoDbConfig {

    @Bean
    fun rouletteGameViewMongoTemplate(rouletteGameViewMongoTemplateFactory: MongoDbFactory): MongoTemplate {
        return MongoTemplate(rouletteGameViewMongoTemplateFactory)
    }

    @Bean
    fun rouletteGameViewMongoTemplateFactory(rouletteGameViewMongoClient: MongoClient, properties: RouletteGameViewMongoConfigurationProperties): MongoDbFactory {
        val rouletteGameViewDb = properties.rouletteGameViewDb
        return SimpleMongoDbFactory(rouletteGameViewMongoClient, rouletteGameViewDb.database)
    }

    @Bean
    fun rouletteGameViewMongoClient(properties: RouletteGameViewMongoConfigurationProperties): MongoClient {
        val casinoServicesViewDb = properties.rouletteGameViewDb
        return MongoClient(casinoServicesViewDb.host, casinoServicesViewDb.port.toInt())
    }

    @Bean
    fun rouletteGameViewTransactionManager(rouletteGameViewMongoTemplateFactory: MongoDbFactory): MongoTransactionManager {
        return MongoTransactionManager(rouletteGameViewMongoTemplateFactory)
    }

}

@Configuration
class RouletteGameViewMongoBee {

    @Bean
    fun rouletteGameViewMongobee(properties: RouletteGameViewMongoConfigurationProperties): Mongobee {
        val rouletteGameViewDb = properties.rouletteGameViewDb
        val dbUri = "mongodb://${rouletteGameViewDb.host}:${rouletteGameViewDb.port}/${rouletteGameViewDb.database}"
        val runner = Mongobee(dbUri)
        runner.setDbName(rouletteGameViewDb.database)
        runner.setChangeLogsScanPackage(RouletteGameViewMongoConfigurationProperties::class.java.`package`.name)
        return runner
    }

}

@Configuration
@ConfigurationProperties(prefix = "mongodb")
class RouletteGameViewMongoConfigurationProperties {

    var rouletteGameViewDb: MongoProperties = MongoProperties()

}