package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.TableReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.TableReservedId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.samples.SamplePointInTime.samplePointInTime

class SampleTableReserved {

    static TableReserved sampleTableReserved(customProperties = [:]) {
        def properties = [
                id: sampleTableReservedId(),
                tableId: sampleTableId(),
                clientId: sampleClientId(),
                occuredAt: samplePointInTime()
        ] + customProperties
        return new TableReserved(
                properties.id as TableReservedId,
                properties.tableId as TableId,
                properties.clientId as ClientId,
                properties.occuredAt as Instant
        )
    }

    static TableReservedId sampleTableReservedId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new TableReservedId(properties.value as String)
    }

}
