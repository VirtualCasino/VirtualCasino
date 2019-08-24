package pl.edu.pollub.virtualcasino.clientservices.table.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReserveRouletteTable

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens

class SampleReserveTable {

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
