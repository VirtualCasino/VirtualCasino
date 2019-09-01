package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class ClientAlreadyParticipated(val clientId: ClientId, val tableId: TableId) : DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("clientId", clientId.value.toString()),
                Pair("tableId", tableId.value.toString())
        ),
        "Client with id: ${clientId.value} already participated to table with id: ${tableId.value}") {

    companion object {
        const val CODE = "casinoServices.table.clientAlreadyParticipated"
    }
}