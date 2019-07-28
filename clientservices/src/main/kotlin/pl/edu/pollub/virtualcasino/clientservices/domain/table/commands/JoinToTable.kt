package pl.edu.pollub.virtualcasino.clientservices.domain.table.commands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

data class JoinToTable(val clientId: ClientId, val tableId: TableId)