package pl.edu.pollub.virtualcasino.table.samples.events

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.Tokens
import pl.edu.pollub.virtualcasino.table.TableId
import pl.edu.pollub.virtualcasino.table.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.table.events.PokerTableReservedId
import pl.edu.pollub.virtualcasino.table.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.table.events.RouletteTableReservedId

import java.time.Instant

import static java.util.UUID.randomUUID
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.table.samples.SampleTable.sampleTableId

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
                value: randomUUID()
        ] + customProperties
        return new RouletteTableReservedId(properties.value as UUID)
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
                value: randomUUID()
        ] + customProperties
        return new PokerTableReservedId(properties.value as UUID)
    }

}
