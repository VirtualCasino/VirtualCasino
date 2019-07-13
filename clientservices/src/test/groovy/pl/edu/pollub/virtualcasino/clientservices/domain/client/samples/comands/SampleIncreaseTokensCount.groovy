package pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.commands.IncreaseTokensCount

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens

class SampleIncreaseTokensCount {

    static IncreaseTokensCount sampleIncreaseTokensCount(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId(),
                tokens: sampleTokens()
        ] + customProperties
        return new IncreaseTokensCount(
                properties.clientId as ClientId,
                properties.tokens as Tokens
        )
    }

}
