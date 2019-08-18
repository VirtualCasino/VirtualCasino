package pl.edu.pollub.virtualcasino.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.table.TableId

class TableNotExist(val tableId: TableId): DomainObjectNotExist("Table with id: ${tableId.value} doesn't exist") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("clientId", tableId.value.toString()))

    companion object {
        const val CODE = "table.tableNotExist"
    }
}