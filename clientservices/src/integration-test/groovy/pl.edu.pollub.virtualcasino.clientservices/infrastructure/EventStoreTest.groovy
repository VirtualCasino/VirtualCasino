package pl.edu.pollub.virtualcasino.clientservices.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.edu.pollub.virtualcasino.clientservices.CasinoServicesBoundedContext

import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventStore
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleEventDescriptor.sampleEventDescriptor

@SpringBootTest(classes = [CasinoServicesBoundedContext.class])
class EventStoreTest extends Specification {

    @Subject
    @Autowired
    EventStore eventStore

    def cleanup() {
        eventStore.clear()
    }

    def 'should get stored events by aggregate id'() {
        given:
            def aggregateId = "aggregateId"
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
            def aggregateId = "aggregateId"
            def events = []
            eventStore.saveEvents(aggregateId, events)
        when:
            def eventStream = eventStore.getEventsOfAggregate(aggregateId)
        then:
            eventStream.events == events
    }

    def 'should get stored events of multiple aggregates by their ids'() {
        given:
            def aggregateId1 = "aggregateId1"
            def events1 = [sampleEventDescriptor(aggregateId: aggregateId1)]
            eventStore.saveEvents(aggregateId1, events1)
        and:
            def aggregateId2 = "aggregateId2"
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
