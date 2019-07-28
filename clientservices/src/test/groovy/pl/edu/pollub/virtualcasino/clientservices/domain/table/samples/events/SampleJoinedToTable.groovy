package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.events

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.JoinedTableId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.samples.SamplePointInTime.samplePointInTime

class SampleJoinedToTable {

    static JoinedTable sampleJoinedTable(customProperties = [:]) {
        def properties = [
                id: sampleJoinedTableId(),
                tableId: sampleTableId(),
                clientId: sampleClientId(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new JoinedTable(
                properties.id as JoinedTableId,
                properties.tableId as TableId,
                properties.clientId as ClientId,
                properties.occurredAt as Instant
        )
    }

    static JoinedTableId sampleJoinedTableId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new JoinedTableId(properties.value as String)
    }

}
