package pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType.*

class SampleReserveTable {

    static ReserveTable sampleReserveTable(customProperties = [:]) {
        def properties = [
                clientId: sampleClientId(),
                gameType: ROULETTE,
                initialBidingRate: sampleTokens()
        ] + customProperties
        return new ReserveTable(
                properties.clientId as ClientId,
                properties.gameType as GameType,
                properties.initialBidingRate as Tokens
        )
    }

}
