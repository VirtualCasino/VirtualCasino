package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.client.fakes.FakedClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.domain.table.Table
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRequirementsFactory

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId

class SampleTable {

    static Table sampleTable(customProperties = [:]) {
        def properties = [
                id: sampleTableId(),
                changes: [],
                tableRequirementsFactory: new TableRequirementsFactory(),
                clientRepository: new FakedClientRepository()
        ] + customProperties
        return new Table(
                properties.id as TableId,
                properties.changes as List,
                properties.tableRequirementsFactory as TableRequirementsFactory,
                properties.clientRepository as ClientRepository
        )
    }

    static TableId sampleTableId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new TableId(properties.value as String)
    }

    static Participation sampleParticipation(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId()
        ] + customProperties
        return new Participation(properties.clientId as ClientId)
    }

}
