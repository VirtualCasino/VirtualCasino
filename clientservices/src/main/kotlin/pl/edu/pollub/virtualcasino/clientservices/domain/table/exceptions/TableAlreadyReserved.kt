package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.lang.IllegalStateException

class TableAlreadyReserved(val clientId: ClientId, val tableId: TableId, val reservedBy: ClientId)
    : IllegalStateException("Client with id: ${clientId.value} can't reserve table with id: ${tableId.value} because it's already reserved by client: ${reservedBy.value}")