package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class InitialBidingRateMustBePositive(val clientId: ClientId, val initialBidingRate: Tokens) : DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("clientId", clientId.value.toString()),
                Pair("initialBidingRate", initialBidingRate.count.toString())
        ),
        "Client with id: ${clientId.value} can't reserve table with initial biding rate: ${initialBidingRate.count} because initial biding rate must be positive") {

    companion object {
        const val CODE = "casinoServices.table.initialBidingRateMustBePositive"
    }

}