package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

class TableFull(val clientId: ClientId, val tableId: TableId, val maxParticipantsCount: Int)
    : DomainObjectInvalidUsed("Client with id ${clientId.value} can't join table with id: ${tableId.value} because this table can have only: $maxParticipantsCount participants") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value.toString()),
            Pair("tableId", tableId.value.toString()),
            Pair("maxParticipantsCount", maxParticipantsCount.toString())
    )

    companion object {
        const val CODE = "table.tableFull"
    }

}