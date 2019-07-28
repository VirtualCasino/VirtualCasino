package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

class TableNotExist(val tableId: TableId): DomainObjectNotExist("Table with id: ${tableId.value} doesn't exist") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("clientId", tableId.value))

    companion object {
        const val CODE = "table.tableNotExist"
    }
}