package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class RouletteGameForTableNotExist(private val tableId: TableId): DomainObjectNotExist(
        CODE,
        mapOf(Pair("clientId", tableId.value.toString())),
        "Roulette game for table with id: ${tableId.value} doesn't exist") {

    companion object {
        const val CODE = "rouletteGame.rouletteGame.rouletteGameForTableNotExists"
    }
}