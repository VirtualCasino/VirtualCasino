package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.lang.IllegalStateException

class TableNotReserved(val clientId: ClientId, val tableId: TableId)
    : IllegalStateException("Client with id: ${clientId.value} can't join table with id: ${tableId.value} because it isn't reserved")