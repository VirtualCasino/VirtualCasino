package pl.edu.pollub.virtualcasino.clientservices.client.events

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.client.samples.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.client.samples.events.TokensBoughtId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens

class SampleTokensBought {

    static TokensBought sampleTokensBought(customProperties = [:]) {
        def properties = [
                id: sampleTokensBoughtId(),
                clientId: sampleClientId(),
                tokens: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new TokensBought(
                properties.id as TokensBoughtId,
                properties.clientId as ClientId,
                properties.tokens as Tokens,
                properties.occurredAt as Instant
        )
    }

    static TokensBoughtId sampleTokensBoughtId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new TokensBoughtId(properties.value as UUID)
    }

}
