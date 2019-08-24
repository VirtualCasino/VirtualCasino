package pl.edu.pollub.virtualcasino.clientservices.table.samples.events

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId

class SampleJoinedToTable {

    static JoinedTable sampleJoinedTable(customProperties = [:]) {
        def properties = [
                id: sampleJoinedTableId(),
                tableId: sampleTableId(),
                clientId: sampleClientId(),
                clientTokens: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new JoinedTable(
                properties.id as JoinedTableId,
                properties.tableId as TableId,
                properties.clientId as ClientId,
                properties.clientTokens as Tokens,
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
