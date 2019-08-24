package pl.edu.pollub.virtualcasino.clientservices.table.commands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

data class JoinTable(val clientId: ClientId, val tableId: TableId)