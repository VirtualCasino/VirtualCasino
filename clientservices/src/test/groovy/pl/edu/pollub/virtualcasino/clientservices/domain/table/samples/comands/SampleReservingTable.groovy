package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveRouletteTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens

class SampleReservingTable {

    static ReserveRouletteTable sampleReserveRouletteTable(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId()
        ] + customProperties
        return new ReserveRouletteTable(
                properties.clientId as ClientId
        )
    }

    static ReservePokerTable sampleReservePokerTable(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId(),
                initialBidingRate: sampleTokens()
        ] + customProperties
        return new ReservePokerTable(
                properties.clientId as ClientId,
                properties.initialBidingRate as Tokens
        )
    }

}
