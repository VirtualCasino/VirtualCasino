package pl.edu.pollub.virtualcasino.clientservices.table.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.commands.JoinToTable

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId


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
