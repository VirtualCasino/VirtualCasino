package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.roulettegame.RouletteGameId
import java.util.*

data class TableId(val value: UUID = UUID.randomUUID()) {

    constructor(gameId: RouletteGameId): this(gameId.value)

}