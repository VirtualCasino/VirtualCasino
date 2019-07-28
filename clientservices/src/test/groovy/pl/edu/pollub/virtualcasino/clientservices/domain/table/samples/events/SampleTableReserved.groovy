package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.events

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.PokerTableReservedId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.RouletteTableReservedId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.samples.SamplePointInTime.samplePointInTime

class SampleTableReserved {

    static RouletteTableReserved sampleRouletteTableReserved(customProperties = [:]) {
        def properties = [
                id: sampleRouletteTableReservedId(),
                tableId: sampleTableId(),
                clientId: sampleClientId(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new RouletteTableReserved(
                properties.id as RouletteTableReservedId,
                properties.tableId as TableId,
                properties.clientId as ClientId,
                properties.occurredAt as Instant
        )
    }

    static RouletteTableReservedId sampleRouletteTableReservedId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new RouletteTableReservedId(properties.value as String)
    }

    static PokerTableReserved samplePokerTableReserved(customProperties = [:]) {
        def properties = [
                id: samplePokerTableReservedId(),
                tableId: sampleTableId(),
                clientId: sampleClientId(),
                initialBidingRate: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new PokerTableReserved(
                properties.id as PokerTableReservedId,
                properties.tableId as TableId,
                properties.clientId as ClientId,
                properties.initialBidingRate as Tokens,
                properties.occurredAt as Instant
        )
    }

    static PokerTableReservedId samplePokerTableReservedId(customProperties = [:]) {
        def properties = [
                value: randomUUID().toString()
        ] + customProperties
        return new PokerTableReservedId(properties.value as String)
    }

}
