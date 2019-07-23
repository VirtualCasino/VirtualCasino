package pl.edu.pollub.virtualcasino.clientservices.domain.client.samples

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.ClientCreated
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.ClientCreatedId

import java.time.Instant


class SampleClientCreated {

    static ClientCreated sampleClientCreated(customProperties = [:]) {
        def properties = [
                id: sampleClientCreatedId(),
                clientId: sampleClientId(),
                occuredAt: samplePointInTime()
        ] + customProperties
        return new ClientCreated(
                properties.id as ClientCreatedId,
                properties.clientId as ClientId,
                properties.occuredAt as Instant
        )
    }

    static ClientCreatedId sampleClientCreatedId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new ClientCreatedId(properties.value as String)
    }
}
