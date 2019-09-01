package pl.edu.pollub.virtualcasino.clientservices.table.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class TableNotExist(val tableId: TableId): DomainObjectNotExist(
        CODE,
        mapOf(Pair("tableId", tableId.value.toString())),
        "Table with id: ${tableId.value} doesn't exist") {

    companion object {
        const val CODE = "clientServices.table.tableNotExist"
    }
}