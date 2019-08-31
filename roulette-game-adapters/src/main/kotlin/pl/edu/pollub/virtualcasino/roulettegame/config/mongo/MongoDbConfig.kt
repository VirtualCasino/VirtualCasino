package pl.edu.pollub.virtualcasino.roulettegame.config.mongo

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
class RouletteGameBoundedContextMongoDbConfig {

    @Bean
    fun rouletteGameBoundedContextMongoTemplate(rouletteGameBoundedContextMongoTemplateFactory: MongoDbFactory): MongoTemplate {
        return MongoTemplate(rouletteGameBoundedContextMongoTemplateFactory)
    }

    @Bean
    fun rouletteGameBoundedContextMongoTemplateFactory(rouletteGameBoundedContextMongoClient: MongoClient, properties: RouletteGameBoundedContextMongoConfigurationProperties): MongoDbFactory {
        val rouletteGameDb = properties.rouletteGameDb
        return SimpleMongoDbFactory(rouletteGameBoundedContextMongoClient, rouletteGameDb.database)
    }

    @Bean
    fun rouletteGameBoundedContextMongoClient(properties: RouletteGameBoundedContextMongoConfigurationProperties): MongoClient {
        val rouletteGameDb = properties.rouletteGameDb
        return MongoClient(rouletteGameDb.host, rouletteGameDb.port.toInt())
    }

    @Bean
    fun rouletteGameBoundedContextTransactionManager(rouletteGameBoundedContextMongoTemplateFactory: MongoDbFactory): MongoTransactionManager {
        return MongoTransactionManager(rouletteGameBoundedContextMongoTemplateFactory)
    }


}

@Configuration
class RouletteGameBoundedContextMongoBee {

    @Bean
    fun rouletteGameBoundedContextMongobee(properties: RouletteGameBoundedContextMongoConfigurationProperties): Mongobee {
        val rouletteGameDb = properties.rouletteGameDb
        val dbUri = "mongodb://${rouletteGameDb.host}:${rouletteGameDb.port}/${rouletteGameDb.database}"
        val runner = Mongobee(dbUri)
        runner.setDbName(rouletteGameDb.database)
        runner.setChangeLogsScanPackage(RouletteGameBoundedContextDatabaseChangelog::class.java.`package`.name)
        return runner
    }

}

@Configuration
@ConfigurationProperties(prefix = "mongodb")
class RouletteGameBoundedContextMongoConfigurationProperties {

    var rouletteGameDb: MongoProperties = MongoProperties()

}