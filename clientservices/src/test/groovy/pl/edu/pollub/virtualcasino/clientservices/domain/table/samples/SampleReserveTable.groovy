package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTableId

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId

class SampleReserveTable {

    static ReserveTable sampleReserveTable(customProperties = [:]) {
        def properties = [
                id: sampleReserveTableId(),
                clientId: sampleClientId()
        ] + customProperties
        return new ReserveTable(
                properties.id as ReserveTableId,
                properties.clientId as ClientId
        )
    }

    static ReserveTableId sampleReserveTableId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new ReserveTableId(properties.value as String)
    }

}
