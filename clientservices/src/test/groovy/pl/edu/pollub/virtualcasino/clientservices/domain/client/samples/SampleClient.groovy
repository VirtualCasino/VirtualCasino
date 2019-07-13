package pl.edu.pollub.virtualcasino.clientservices.domain.client.samples

import pl.edu.pollub.virtualcasino.clientservices.domain.client.Client
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes.FakedTableRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository

import static java.util.UUID.randomUUID

class SampleClient {

    static Client sampleClient(customProperties = [:]) {
        def properties = [
                id: sampleClientId(),
                changes: [],
                tableFactory: new TableFactory(),
                tableRepository: new FakedTableRepository()
        ] + customProperties
        return new Client(
                properties.id as ClientId,
                properties.changes as List,
                properties.tableFactory as TableFactory,
                properties.tableRepository as TableRepository
        )
    }

    static ClientId sampleClientId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new ClientId(properties.value as String)
    }
}
