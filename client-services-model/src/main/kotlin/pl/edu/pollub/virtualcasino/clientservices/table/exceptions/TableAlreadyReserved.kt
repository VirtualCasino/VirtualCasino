package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class TableAlreadyReserved(val clientId: ClientId, val tableId: TableId, val reservedBy: ClientId)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't reserve table with id: ${tableId.value} because it's already reserved by client: ${reservedBy.value}") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value.toString()),
            Pair("tableId", tableId.value.toString()),
            Pair("reservedBy", reservedBy.value.toString())
    )

    companion object {
        const val CODE = "casinoServices.table.tableAlreadyReserved"
    }

}