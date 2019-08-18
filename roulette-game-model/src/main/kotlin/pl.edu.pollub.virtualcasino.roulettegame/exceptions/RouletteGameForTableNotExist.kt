package pl.edu.pollub.virtualcasino.roulettegame.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.clientservices.table.TableId

class RouletteGameForTableNotExist(val tableId: TableId): DomainObjectNotExist("Roulette game for table with id: ${tableId.value} doesn't exist") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("clientId", tableId.value.toString()))

    companion object {
        const val CODE = "rouletteGame.rouletteGame.rouletteGameForTableNotExists"
    }
}