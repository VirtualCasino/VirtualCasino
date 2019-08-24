package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class InitialBidingRateTooHigh(val clientId: ClientId, val tokens: Tokens, val initialBidingRate: Tokens)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't join table because initial biding rate: ${initialBidingRate.count} is higher than their tokens count: ${tokens.count}") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value.toString()),
            Pair("tokens", tokens.count.toString()),
            Pair("initialBidingRate", initialBidingRate.count.toString())
    )

    companion object {
        const val CODE = "casinoServices.table.initialBidingRateToHigh"
    }
}