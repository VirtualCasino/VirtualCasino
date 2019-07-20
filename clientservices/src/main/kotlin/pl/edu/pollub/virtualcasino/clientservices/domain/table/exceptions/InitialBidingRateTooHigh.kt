package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

class InitialBidingRateTooHigh(val clientId: ClientId, val tableId: TableId, val tokens: Tokens, val initialBidingRate: Tokens)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't join table with id: ${tableId.value} because initial biding rate: ${initialBidingRate.count} is higher than their tokens count: ${tokens.count}") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value),
            Pair("tableId", tableId.value),
            Pair("tokens", tokens.count.toString()),
            Pair("initialBidingRate", initialBidingRate.count.toString())
    )

    companion object {
        const val CODE = "table.initialBidingRateToHigh"
    }
}