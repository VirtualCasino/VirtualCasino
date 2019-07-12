package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples

import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventDescriptor

import java.time.Instant

import static pl.edu.pollub.virtualcasino.clientservices.samples.SamplePointInTime.samplePointInTime

class SampleEventDescriptor {

    static EventDescriptor sampleEventDescriptor(customProperties = [:]) {
        def properties = [
                id: "id",
                body: "body",
                occurredAt: samplePointInTime(),
                type: "type",
                aggregateId: "aggregateId"
        ] + customProperties
        return new EventDescriptor(
                properties.id as String,
                properties.body as String,
                properties.occurredAt as Instant,
                properties.type as String,
                properties.aggregateId as String
        )
    }

}
