package pl.edu.pollub.virtualcasino.clientservices.domain.client.samples

import pl.edu.pollub.virtualcasino.clientservices.domain.client.commands.CreateClient
import pl.edu.pollub.virtualcasino.clientservices.domain.client.commands.CreateClientId

import static java.util.UUID.randomUUID

class SampleCreateClient {

    static CreateClient sampleCreateClient(customProperties = [:]) {
        def properties = [
                id: sampleCreateClientId()
        ]
        return new CreateClient()
    }

    static CreateClientId sampleCreateClientId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new CreateClientId(properties.value as String)
    }
}
