package pl.edu.pollub.virtualcasino.table.samples.comands

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.Tokens
import pl.edu.pollub.virtualcasino.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.table.commands.ReserveRouletteTable

import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.sampleTokens

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
