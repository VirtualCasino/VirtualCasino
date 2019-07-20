package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

class InitialBidingRateCantBeZero(val clientId: ClientId, val tableId: TableId)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't reserve table with id: ${tableId.value} because initial biding rate can't be equal to zero") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value),
            Pair("tableId", tableId.value)
    )

    companion object {
        const val CODE = "table.initialBidingRateCantBeZero"
    }

}