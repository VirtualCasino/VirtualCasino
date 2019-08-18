package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class InitialBidingRateMustBePositive(val clientId: ClientId, val tableId: TableId, val initialBidingRate: Tokens)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't reserve table with id: ${tableId.value} with initial biding rate: ${initialBidingRate.count} because initial biding rate must be positive") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value.toString()),
            Pair("tableId", tableId.value.toString()),
            Pair("initialBidingRate", initialBidingRate.count.toString())
    )

    companion object {
        const val CODE = "casinoServices.table.initialBidingRateMustBePositive"
    }

}