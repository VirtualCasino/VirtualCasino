package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class TableNotExist(val tableId: TableId): DomainObjectNotExist("Table with id: ${tableId.value} doesn't exist") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("tableId", tableId.value.toString()))

    companion object {
        const val CODE = "clientServices.table.tableNotExist"
    }
}