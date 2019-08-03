package pl.edu.pollub.virtualcasino.clientservices.infrastructure.publisher

import pl.edu.pollub.virtualcasino.clientservices.BaseIntegrationTest
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventDescriptor
import org.springframework.messaging.Message


import static pl.edu.pollub.virtualcasino.clientservices.infrastructure.samples.SampleEventDescriptor.sampleEventDescriptor

class EventPublisherTest extends BaseIntegrationTest {

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