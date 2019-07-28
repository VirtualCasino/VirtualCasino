package pl.edu.pollub.virtualcasino.clientservices.infrastructure.publisher

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.stream.messaging.Source
import org.springframework.cloud.stream.test.binder.MessageCollector
import org.springframework.data.mongodb.core.MongoTemplate
import pl.edu.pollub.virtualcasino.clientservices.BaseIntegrationTest
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventDescriptor
import spock.lang.Subject
import spock.util.concurrent.PollingConditions
import org.springframework.messaging.Message

import java.util.concurrent.BlockingQueue

import static pl.edu.pollub.virtualcasino.clientservices.infrastructure.samples.SampleEventDescriptor.sampleEventDescriptor

class EventPublisherTest extends BaseIntegrationTest {

    @Subject
    @Autowired
    EventPublisher eventPublisher

    @Autowired
    Source source

    @Autowired
    MessageCollector messageCollector

    @Autowired
    @Qualifier("casinoServicesWriteTemplate")
    MongoTemplate mongo

    PollingConditions conditions

    BlockingQueue<Message<?>> channel

    def setup() {
        conditions = new PollingConditions(timeout: 12, initialDelay: 0, factor: 1)
        channel = messageCollector.forChannel(source.output())
    }

    def 'should unpublished pending events'() {
        given:
            EventDescriptor pendingEvent = unpublishedEvent()
        when:
            eventPublisher.publish()
        then:
            conditions.eventually {
                Message<String> received = channel.poll()
                received != null
                received.getPayload() == pendingEvent.getBody()
            }
    }

    EventDescriptor unpublishedEvent() {
        def event = sampleEventDescriptor()
        mongo.save(event)
        return event
    }

}