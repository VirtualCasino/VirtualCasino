package pl.edu.pollub.virtualcasino.table.samples

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.ClientRepository
import pl.edu.pollub.virtualcasino.table.Participation
import pl.edu.pollub.virtualcasino.table.Table
import pl.edu.pollub.virtualcasino.table.TableId
import pl.edu.pollub.virtualcasino.client.fakes.FakedClientRepository

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleClientId

class SampleTable {

    static Table sampleTable(customProperties = [:]) {
        def properties = [
                id: sampleTableId(),
                changes: [],
                clientRepository: new FakedClientRepository()
        ] + customProperties
        return new Table(
                properties.id as TableId,
                properties.clientRepository as ClientRepository,
                properties.changes as List
        )
    }

    static TableId sampleTableId(customProperties = [:]) {
        def properties = [
                value: randomUUID()
        ] + customProperties
        return new TableId(properties.value as UUID)
    }

    static Participation sampleParticipation(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId()
        ] + customProperties
        return new Participation(properties.clientId as ClientId)
    }

}
