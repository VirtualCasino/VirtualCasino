package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.lang.IllegalStateException

class TableFull(val clientId: ClientId, val tableId: TableId, val maxParticipantsCount: Int)
    : IllegalStateException("Client with id ${clientId.value} can't join table with id: ${tableId.value} because this table can have only: $maxParticipantsCount participants")