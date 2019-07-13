package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.JoinToTable

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId

class SampleJoinTable {

    static JoinToTable sampleJoinTable(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId(),
                tableId: sampleTableId()
        ] + customProperties
        return new JoinToTable(
                properties.clientId as ClientId,
                properties.tableId as TableId
        )
    }

}
