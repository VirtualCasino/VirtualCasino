package pl.edu.pollub.virtualcasino.table.commands

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.table.TableId

data class JoinToTable(val clientId: ClientId, val tableId: TableId)