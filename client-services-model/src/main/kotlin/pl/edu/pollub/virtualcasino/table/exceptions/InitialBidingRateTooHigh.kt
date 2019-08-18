package pl.edu.pollub.virtualcasino.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.Tokens
import pl.edu.pollub.virtualcasino.table.TableId

class InitialBidingRateTooHigh(val clientId: ClientId, val tableId: TableId, val tokens: Tokens, val initialBidingRate: Tokens)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't join table with id: ${tableId.value} because initial biding rate: ${initialBidingRate.count} is higher than their tokens count: ${tokens.count}") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value.toString()),
            Pair("tableId", tableId.value.toString()),
            Pair("tokens", tokens.count.toString()),
            Pair("initialBidingRate", initialBidingRate.count.toString())
    )

    companion object {
        const val CODE = "casinoServices.table.initialBidingRateToHigh"
    }
}