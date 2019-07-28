package pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pollub.virtualcasino.clientservices.BaseIntegrationTest

import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventStore
import spock.lang.Subject

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.infrastructure.samples.SampleEventDescriptor.sampleEventDescriptor

class EventStoreTest extends BaseIntegrationTest {

    @Subject
    @Autowired
    EventStore eventStore

    def cleanup() {
        eventStore.clear()
    }

    def 'should get stored events by aggregate id'() {
        given:
            def aggregateId = randomUUID()
            def events = [
                    sampleEventDescriptor(aggregateId: aggregateId),
                    sampleEventDescriptor(aggregateId: aggregateId)
            ]
            eventStore.saveEvents(aggregateId, events)
        when:
            def eventStream = eventStore.getEventsOfAggregate(aggregateId)
        then:
            eventStream.events == events
    }

    def 'should get stored empty events list by aggregate id'() {
        given:
            def aggregateId = randomUUID()
            def events = []
            eventStore.saveEvents(aggregateId, events)
        when:
            def eventStream = eventStore.getEventsOfAggregate(aggregateId)
        then:
            eventStream.events == events
    }

    def 'should get stored events of multiple aggregates by their ids'() {
        given:
            def aggregateId1 = randomUUID()
            def events1 = [sampleEventDescriptor(aggregateId: aggregateId1)]
            eventStore.saveEvents(aggregateId1, events1)
        and:
            def aggregateId2 = randomUUID()
            def events2 = [sampleEventDescriptor(aggregateId: aggregateId2)]
            eventStore.saveEvents(aggregateId2, events2)
        when:
            def eventStream1 = eventStore.getEventsOfAggregate(aggregateId1)
        then:
            eventStream1.events == events1
        when:
            def eventStream2 = eventStore.getEventsOfAggregate(aggregateId2)
        then:
            eventStream2.events == events2
    }
}
