package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class TableAlreadyReserved(val clientId: ClientId, val tableId: TableId) : DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("clientId", clientId.value.toString()),
                Pair("tableId", tableId.value.toString())
        ),
        "Client with id: ${clientId.value} can't reserve table with id: ${tableId.value} because it's already reserved") {

    companion object {
        const val CODE = "casinoServices.table.tableAlreadyReserved"
    }

}