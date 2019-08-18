package pl.edu.pollub.virtualcasino

import com.mongodb.MongoClient
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Versions
import de.flapdoodle.embed.process.distribution.GenericVersion
import org.bson.Document
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.transaction.annotation.EnableTransactionManagement

import static de.flapdoodle.embed.mongo.distribution.Feature.*
import static de.flapdoodle.embed.process.runtime.Network.localhostIsIPv6

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(MongoConfigurationProperties.class)
@EntityScan(basePackageClasses = [AdapterCommonsConfig.class])
class AdapterCommonsConfig {

    private static final String VERSION = "4.0.0"
    private static final String REPLICA_SET_NAME = "rs0"
    private static final String STORAGE_ENGINE_NAME = "wiredTiger"

    @Bean
    MongoTemplate mongoTemplate(MongoClient mongoClient, MongoConfigurationProperties properties) throws IOException {
        def testDb = properties.testDb
        return new MongoTemplate(mongoClient, testDb.database)
    }

    @Bean(destroyMethod = "close")
    MongoClient mongoClient(MongodProcess mongodProcess) throws IOException {
        def connection = mongodProcess.getConfig().net()
        def client = new MongoClient(connection.getServerAddress().getHostName(), connection.getPort())
        client.getDatabase("admin").runCommand(new Document("replSetInitiate", new Document()))
        return client
    }

    @Bean(destroyMethod = "stop")
    MongodProcess mongodProcess(MongodExecutable mongodExecutable) throws IOException {
        return mongodExecutable.start()
    }

    @Bean(destroyMethod = "stop")
    MongodExecutable casinoServicesWriteDbExecutable(IMongodConfig mongodConfig) {
        return MongodStarter.getDefaultInstance().prepare(mongodConfig)
    }

    @Bean
    IMongodConfig mongodConfig(MongoConfigurationProperties properties) throws IOException {
        def testDb = properties.testDb
        def net = new Net(testDb.host, testDb.port, localhostIsIPv6())
        def version = Versions.withFeatures(
                new GenericVersion(VERSION),
                ONLY_WITH_SSL,
                ONLY_64BIT,
                NO_HTTP_INTERFACE_ARG,
                STORAGE_ENGINE,
                MONGOS_CONFIGDB_SET_STYLE,
                NO_CHUNKSIZE_ARG
        )
        def cmdOptions = new MongoCmdOptionsBuilder()
                .useNoPrealloc(false)
                .useSmallFiles(false)
                .useNoJournal(false)
                .useStorageEngine(STORAGE_ENGINE_NAME)
                .verbose(false)
                .build()
        return new MongodConfigBuilder()
                .withLaunchArgument("--replSet", REPLICA_SET_NAME)
                .version(version)
                .net(net)
                .cmdOptions(cmdOptions)
                .build()
    }

    @Bean
    MongodStarter mongodStarter() {
        return MongodStarter.getDefaultInstance()
    }

    @Bean
    MongoTransactionManager transactionManager(MongoTemplate writeTemplate) {
        return new MongoTransactionManager(writeTemplate.getMongoDbFactory())
    }

    @ConfigurationProperties(prefix = "mongodb")
    class MongoConfigurationProperties {

        MongoProperties testDb

    }
}
