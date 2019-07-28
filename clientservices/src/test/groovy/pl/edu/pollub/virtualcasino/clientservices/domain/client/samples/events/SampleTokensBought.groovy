package pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.events

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.TokensBoughtId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.samples.SamplePointInTime.samplePointInTime

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
                value: randomUUID().toString()
        ] + customProperties
        return new TokensBoughtId(properties.value as String)
    }

}
