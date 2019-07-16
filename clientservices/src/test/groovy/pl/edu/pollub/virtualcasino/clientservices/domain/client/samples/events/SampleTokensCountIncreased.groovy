package pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.events

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.TokensCountIncreased
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.TokensCountIncreasedId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.samples.SamplePointInTime.samplePointInTime

class SampleTokensCountIncreased {

    static TokensCountIncreased sampleTokensCountIncreased(customProperties = [:]) {
        def properties = [
                id: sampleTokensCountIncreasedId(),
                clientId: sampleClientId(),
                tokens: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new TokensCountIncreased(
                properties.id as TokensCountIncreasedId,
                properties.clientId as ClientId,
                properties.tokens as Tokens,
                properties.occurredAt as Instant
        )
    }

    static TokensCountIncreasedId sampleTokensCountIncreasedId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new TokensCountIncreasedId(properties.value as String)
    }

}
