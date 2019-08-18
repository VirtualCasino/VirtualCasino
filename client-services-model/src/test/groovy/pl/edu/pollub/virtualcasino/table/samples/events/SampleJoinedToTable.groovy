package pl.edu.pollub.virtualcasino.table.samples.events

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.table.TableId
import pl.edu.pollub.virtualcasino.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.table.events.JoinedTableId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.table.samples.SampleTable.sampleTableId

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
                value: randomUUID()
        ] + customProperties
        return new JoinedTableId(properties.value as UUID)
    }

}
