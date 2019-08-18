package pl.edu.pollub.virtualcasino.clientservices.table.samples

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.table.Table
import pl.edu.pollub.virtualcasino.clientservices.table.TableEventPublisher
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedClientRepository
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedTableEventPublisher

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId

class SampleTable {

    static Table sampleTable(customProperties = [:]) {
        def properties = [
                id: sampleTableId(),
                changes: [],
                clientRepository: new FakedClientRepository(),
                eventPublisher: new FakedTableEventPublisher()
        ] + customProperties
        return new Table(
                properties.id as TableId,
                properties.changes as List,
                properties.clientRepository as ClientRepository,
                properties.eventPublisher as TableEventPublisher
        )
    }

    static Participation sampleParticipation(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId()
        ] + customProperties
        return new Participation(properties.clientId as ClientId)
    }

}
