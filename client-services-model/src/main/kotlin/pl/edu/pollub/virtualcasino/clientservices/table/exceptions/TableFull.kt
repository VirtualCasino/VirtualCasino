package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class TableFull(val clientId: ClientId, val tableId: TableId, val maxParticipantsCount: Int) : DomainObjectInvalidUsed(
        CODE,
        mapOf(
                Pair("clientId", clientId.value.toString()),
                Pair("tableId", tableId.value.toString()),
                Pair("maxParticipantsCount", maxParticipantsCount.toString())
        ),
        "Client with id ${clientId.value} can't join table with id: ${tableId.value} because this table can have only: $maxParticipantsCount participants") {

    companion object {
        const val CODE = "clientServices.table.tableFull"
    }

}