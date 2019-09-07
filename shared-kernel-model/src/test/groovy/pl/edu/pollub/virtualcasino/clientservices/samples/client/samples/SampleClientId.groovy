package pl.edu.pollub.virtualcasino.clientservices.samples.client.samples

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId

import static java.util.UUID.randomUUID

class SampleClientId {

    static ClientId sampleClientId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new ClientId(properties.value as UUID)
    }

}
