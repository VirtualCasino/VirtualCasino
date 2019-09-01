package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.InitialBidingRateMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.InitialBidingRateTooHigh

internal class InitialBiddingRateRules {

    internal fun isInitialBiddingRateValid(command: ReservePokerTable, client: Client) {
        val clientId = command.clientId()
        if (command.initialBidingRate <= Tokens()) throw InitialBidingRateMustBePositive(clientId, command.initialBidingRate)
        if (client.tokens() < command.initialBidingRate) throw InitialBidingRateTooHigh(clientId, client.tokens(), command.initialBidingRate)
    }

}