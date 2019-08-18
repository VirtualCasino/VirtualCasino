package pl.edu.pollub.virtualcasino.table.samples.comands

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.table.TableId
import pl.edu.pollub.virtualcasino.table.commands.JoinToTable

import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.table.samples.SampleTable.sampleTableId

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
