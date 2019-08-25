package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import java.util.*

data class RouletteGameId(val value: UUID = UUID.randomUUID()) {

    constructor(tableId: TableId) : this(tableId.value)

}