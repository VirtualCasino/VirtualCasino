package pl.edu.pollub.virtualcasino.client.samples

import pl.edu.pollub.virtualcasino.client.Client
import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.Tokens
import pl.edu.pollub.virtualcasino.table.TableRepository
import pl.edu.pollub.virtualcasino.table.fakes.FakedTableRepository

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
                properties.tableRepository as TableRepository,
                properties.changes as List
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
