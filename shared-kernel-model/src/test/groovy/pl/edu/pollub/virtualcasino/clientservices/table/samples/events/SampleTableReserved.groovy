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

class SampleTableReserved {

    static RouletteTableReserved sampleRouletteTableReserved(customProperties = [:]) {
        def properties = [
                id: sampleRouletteTableReservedId(),
                tableId: sampleTableId(),
                clientId: sampleClientId(),
                clientTokens: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new RouletteTableReserved(
                properties.id as RouletteTableReservedId,
                properties.tableId as TableId,
                properties.clientId as ClientId,
                properties.clientTokens as Tokens,
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
                clientTokens: sampleTokens(),
                occurredAt: samplePointInTime()
        ] + customProperties
        return new PokerTableReserved(
                properties.id as PokerTableReservedId,
                properties.tableId as TableId,
                properties.clientId as ClientId,
                properties.initialBidingRate as Tokens,
                properties.clientTokens as Tokens,
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
