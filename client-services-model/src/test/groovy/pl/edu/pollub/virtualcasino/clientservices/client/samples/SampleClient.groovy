package pl.edu.pollub.virtualcasino.clientservices.client.samples

import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.ClientEventPublisher
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedClientEventPublisher
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId

import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedTableRepository

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId

class SampleClient {

    static Client sampleClient(customProperties = [:]) {
        def properties = [
                id: sampleClientId(),
                changes: [],
                tableRepository: new FakedTableRepository(),
                eventPublisher: new FakedClientEventPublisher()
        ] + customProperties
        return new Client(
                properties.id as ClientId,
                properties.changes as List,
                properties.tableRepository as TableRepository,
                properties.eventPublisher as ClientEventPublisher
        )
    }

}
