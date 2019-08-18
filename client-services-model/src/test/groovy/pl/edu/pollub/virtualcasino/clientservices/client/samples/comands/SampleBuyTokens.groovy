package pl.edu.pollub.virtualcasino.clientservices.client.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens

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
