package pl.edu.pollub.virtualcasino.clientservices.domain.table.commands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import java.util.*

data class ReserveTable(val id: ReserveTableId = ReserveTableId(), val clientId: ClientId)

data class ReserveTableId(val value: String = UUID.randomUUID().toString())