package pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.events

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Nick
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.client.events.ClientRegistered
import pl.edu.pollub.virtualcasino.clientservices.client.events.ClientRegisteredId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleNick.sampleNick
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens

class SampleClientRegistered {

    static ClientRegistered sampleClientRegistered(customProperties = [:]) {
        def properties = [
                id: sampleClientRegisteredId(),
                clientId: sampleClientId(),
                nick: sampleNick(),
                initialTokens: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new ClientRegistered(
                properties.id as ClientRegisteredId,
                properties.clientId as ClientId,
                properties.nick as Nick,
                properties.initialTokens as Tokens,
                properties.occurredAt as Instant
        )
    }

    static ClientRegisteredId sampleClientRegisteredId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new ClientRegisteredId(properties.value as UUID)
    }

}
