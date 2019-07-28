package pl.edu.pollub.virtualcasino.clientservices.domain.client.samples

import pl.edu.pollub.virtualcasino.clientservices.domain.client.Client
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes.FakedTableRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository

import static java.util.UUID.randomUUID

class SampleClient {

    static Client sampleClient(customProperties = [:]) {
        def properties = [
                id: sampleClientId(),
                changes: [],
                tableRepository: new FakedTableRepository()
        ] + customProperties
        return new Client(
                properties.id as ClientId,
                properties.changes as List,
                properties.tableRepository as TableRepository
        )
    }

    static ClientId sampleClientId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new ClientId(properties.value as UUID)
    }

    static Tokens sampleTokens(customProperties = [:]) {
        def properties = [
                count: 0
        ] + customProperties
        return new Tokens(properties.count as Integer)
    }
}
