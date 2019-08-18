package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class ClientAlreadyParticipated(val clientId: ClientId, val tableId: TableId)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} already participated to table with id: ${tableId.value}") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value.toString()),
            Pair("tableId", tableId.value.toString())
    )

    companion object {
        const val CODE = "casinoServices.table.clientAlreadyParticipated"
    }
}