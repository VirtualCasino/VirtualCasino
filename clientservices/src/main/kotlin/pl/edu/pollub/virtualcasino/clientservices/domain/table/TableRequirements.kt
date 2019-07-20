package pl.edu.pollub.virtualcasino.clientservices.domain.table

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Client
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType.POKER
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType.ROULETTE
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.TableReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.InitialBidingRateTooHigh
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.TableFull

interface TableRequirements {

    fun check(client: Client): Boolean

}

class NoRequirements: TableRequirements {

    override fun check(client: Client): Boolean {
        return true
    }

}

class RouletteTableRequirements(private val tableId: TableId, private val participation: List<Participation>): TableRequirements {

    override fun check(client: Client): Boolean {
        if(participation.count() >= MAX_ROULETTE_PARTICIPANTS_COUNT) throw TableFull(client.id, tableId, MAX_ROULETTE_PARTICIPANTS_COUNT)
        return true
    }

    companion object {
        const val MAX_ROULETTE_PARTICIPANTS_COUNT = 10
    }

}

class PokerTableRequirements(private val tableId: TableId, private val participation: List<Participation>, private val initialBidingRate: Tokens): TableRequirements {

    override fun check(client: Client): Boolean {
        if(participation.count() >= MAX_POKER_PARTICIPANTS_COUNT) throw TableFull(client.id, tableId, MAX_POKER_PARTICIPANTS_COUNT)
        if(client.tokens() < initialBidingRate) throw InitialBidingRateTooHigh(client.id, tableId, client.tokens(), initialBidingRate)
        return true
    }

    companion object {
        const val MAX_POKER_PARTICIPANTS_COUNT = 10
    }

}

@Component
class TableRequirementsFactory {

    fun create(event: TableReserved, participation: List<Participation>): TableRequirements {
        return when(event.gameType) {
            ROULETTE -> RouletteTableRequirements(event.tableId, participation)
            POKER -> PokerTableRequirements(event.tableId, participation, event.initialBidingRate)
        }
    }

}

