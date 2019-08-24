package pl.edu.pollub.virtualcasino.clientservices.table.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.commands.JoinTable

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId


class SampleJoinTable {

    static JoinTable sampleJoinTable(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId(),
                tableId: sampleTableId()
        ] + customProperties
        return new JoinTable(
                properties.clientId as ClientId,
                properties.tableId as TableId
        )
    }

}
