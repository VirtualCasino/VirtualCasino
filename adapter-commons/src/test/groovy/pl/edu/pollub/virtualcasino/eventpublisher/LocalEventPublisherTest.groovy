package pl.edu.pollub.virtualcasino.eventpublisher

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.BaseIntegrationTest
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import spock.lang.Subject

import java.time.Instant

import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime

class LocalEventPublisherTest extends BaseIntegrationTest {

    @Subject
    LocalEventPublisher eventPublisher = new LocalEventPublisherTestImpl()

    def 'should publish event'() {
        given:
            def event = new DummyEvent()
            def listener = new DummyEventListener()
            eventPublisher.subscribe(listener)
        when:
            eventPublisher.publish(event)
        then:
            conditions.eventually {
                def listenedEvent = listener.listenedEvents.first()
                listenedEvent == event
            }
    }

}

class LocalEventPublisherTestImpl extends LocalEventPublisher {

}

class DummyEvent implements DomainEvent {

    static def TYPE = "DummyEvent"

    @Override
    String type() {
        return TYPE
    }

    @Override
    Instant occurredAt() {
        return samplePointInTime()
    }

    @Override
    UUID aggregateId() {
        return UUID.randomUUID()
    }
}

class DummyEventListener implements DomainEventListener<DummyEvent> {

    List<DummyEvent> listenedEvents = []

    @Override
    boolean isListenFor(@NotNull DomainEvent event) {
        return event.type() == DummyEvent.TYPE
    }

    @Override
    void reactTo(@NotNull DomainEvent event) {
        listenedEvents.add((DummyEvent) event)
    }
}
