package pl.edu.pollub.virtualcasino.client.samples.comands

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.Tokens
import pl.edu.pollub.virtualcasino.client.commands.BuyTokens

import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleTokens

class SampleBuyTokens {

    static BuyTokens sampleBuyTokens(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId(),
                tokens: sampleTokens()
        ] + customProperties
        return new BuyTokens(
                properties.clientId as ClientId,
                properties.tokens as Tokens
        )
    }

}
