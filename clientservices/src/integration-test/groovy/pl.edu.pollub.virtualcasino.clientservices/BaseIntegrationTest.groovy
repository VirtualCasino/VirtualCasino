package pl.edu.pollub.virtualcasino.clientservices

import com.mongodb.MongoClient
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.IMongodConfig
import de.flapdoodle.embed.mongo.config.MongoCmdOptionsBuilder
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import org.bson.Document
import de.flapdoodle.embed.mongo.distribution.Versions
import de.flapdoodle.embed.process.distribution.GenericVersion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.cloud.stream.messaging.Source
import org.springframework.cloud.stream.test.binder.MessageCollector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.messaging.Message
import org.springframework.test.context.ActiveProfiles
import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.mongo.MongoConfigurationProperties
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.publisher.EventPublisher
import spock.lang.Specification
import spock.lang.Subject
import spock.util.concurrent.PollingConditions

import java.util.concurrent.BlockingQueue

import static de.flapdoodle.embed.mongo.distribution.Feature.*
import static de.flapdoodle.embed.process.runtime.Network.localhostIsIPv6
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [CasinoServicesBoundedContext.class])
@ActiveProfiles("integration-test")
class BaseIntegrationTest extends Specification {

    @Autowired
    TestRestTemplate http

    @Subject
    @Autowired
    EventPublisher eventPublisher

    @Autowired
    Source source

    @Autowired
    MessageCollector messageCollector

    @Autowired
    EventSerializer serializer


    @Autowired
    @Qualifier("casinoServicesWriteTemplate")
    MongoTemplate mongo

    PollingConditions conditions

    BlockingQueue<Message<?>> channel

    def setup() {
        conditions = new PollingConditions(timeout: 12, initialDelay: 0, factor: 1)
        channel = messageCollector.forChannel(source.output())
    }

    def <T extends DomainEvent> T getEvent(Class<T> eventClass) {
        Message<String> received = channel.poll()
        if(received == null) return null
        def event = serializer.deserialize(received.getPayload())
        if(event.getClass() != eventClass) return null
        return eventClass.cast(event)
    }
}

@Configuration
@Profile("integration-test")
class MongoDbTestConfig {

    private static final String VERSION = "4.0.0"
    private static final String REPLICA_SET_NAME = "rs0"
    private static final String STORAGE_ENGINE_NAME = "wiredTiger"

    @Bean
    MongoTemplate casinoServicesWriteTemplate(MongoClient casinoServicesWriteDbMongoClient, MongoConfigurationProperties properties) throws IOException {
        def casinoServicesWriteDb = properties.casinoServicesWriteDb
        return new MongoTemplate(casinoServicesWriteDbMongoClient, casinoServicesWriteDb.database)
    }

    @Bean(destroyMethod = "close")
    MongoClient casinoServicesWriteDbMongoClient(MongodProcess casinoServicesWriteDbProcess) throws IOException {
        def connection = casinoServicesWriteDbProcess.getConfig().net()
        def client = new MongoClient(connection.getServerAddress().getHostName(), connection.getPort())
        client.getDatabase("admin").runCommand(new Document("replSetInitiate", new Document()))
        return client
    }

    @Bean(destroyMethod = "stop")
    MongodProcess casinoServicesWriteDbProcess(MongodExecutable casinoServicesWriteDbExecutable) throws IOException {
        return casinoServicesWriteDbExecutable.start()
    }

    @Bean(destroyMethod = "stop")
    MongodExecutable casinoServicesWriteDbExecutable(IMongodConfig casinoServicesWriteDbConfig) {
        return MongodStarter.getDefaultInstance().prepare(casinoServicesWriteDbConfig)
    }

    @Bean
    IMongodConfig casinoServicesWriteDbConfig(MongoConfigurationProperties properties) throws IOException {
        def casinoServicesWriteDb = properties.casinoServicesWriteDb
        def net = new Net(casinoServicesWriteDb.host, casinoServicesWriteDb.port, localhostIsIPv6())
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
    MongoTransactionManager transactionManager(MongoTemplate casinoServicesWriteTemplate) {
        return new MongoTransactionManager(casinoServicesWriteTemplate.getMongoDbFactory())
    }

}