package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.lang.IllegalStateException

class ClientAlreadyParticipated(val clientId: ClientId, val tableId: TableId)
    : IllegalStateException("Client with id: ${clientId.value} already participated to table with id: ${tableId.value}")