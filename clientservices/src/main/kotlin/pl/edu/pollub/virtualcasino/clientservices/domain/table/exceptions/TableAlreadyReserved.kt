package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

class TableAlreadyReserved(val clientId: ClientId, val tableId: TableId, val reservedBy: ClientId)
    : DomainObjectInvalidUsed("Client with id: ${clientId.value} can't reserve table with id: ${tableId.value} because it's already reserved by client: ${reservedBy.value}") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value),
            Pair("tableId", tableId.value),
            Pair("reservedBy", reservedBy.value)
    )

    companion object {
        const val CODE = "table.tableAlreadyReserved"
    }

}