package pl.edu.pollub.virtualcasino.clientservices.samples.client.samples

import pl.edu.pollub.virtualcasino.clientservices.client.Tokens

class SampleTokens {

    static Tokens sampleTokens(customProperties = [:]) {
        def properties = [
                count: 0
        ] + customProperties
        return new Tokens(properties.count as Integer)
    }

}
