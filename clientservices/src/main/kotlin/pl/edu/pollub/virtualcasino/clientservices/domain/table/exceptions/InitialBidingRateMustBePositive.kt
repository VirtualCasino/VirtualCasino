package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

class InitialBidingRateMustBePositive(val clientId: ClientId, val tableId: TableId, val initialBidingRate: Tokens)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't reserve table with id: ${tableId.value} with initial biding rate: ${initialBidingRate.count} because initial biding rate must be positive") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value),
            Pair("tableId", tableId.value),
            Pair("initialBidingRate", initialBidingRate.count.toString())
    )

    companion object {
        const val CODE = "table.initialBidingRateMustBePositive"
    }

}