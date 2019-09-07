package pl.edu.pollub.virtualcasino.eventstore.samples

import pl.edu.pollub.virtualcasino.eventstore.EventDescriptor

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime

class SampleEventDescriptor {

    static EventDescriptor sampleEventDescriptor(customProperties = [:]) {
        def properties = [
                id: randomUUID(),
                body: "body",
                occurredAt: samplePointInTime(),
                type: "type",
                aggregateId: randomUUID()
        ] + customProperties
        return new EventDescriptor(
                properties.id as UUID,
                properties.body as String,
                properties.occurredAt as Instant,
                properties.type as String,
                properties.aggregateId as UUID
        )
    }

}
