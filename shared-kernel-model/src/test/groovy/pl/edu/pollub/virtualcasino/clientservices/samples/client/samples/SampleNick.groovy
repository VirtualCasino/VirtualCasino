package pl.edu.pollub.virtualcasino.clientservices.samples.client.samples

import pl.edu.pollub.virtualcasino.clientservices.client.Nick

class SampleNick {

    static Nick sampleNick(customProperties = [:]) {
        def properties = [
                value: "Test"
        ] + customProperties
        return new Nick(properties.value as String)
    }

}
