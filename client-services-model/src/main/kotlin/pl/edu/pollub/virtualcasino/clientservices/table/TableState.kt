package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.ClientAlreadyParticipated
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.InitialBidingRateTooHigh
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableFull
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableNotReserved

internal interface TableState {

    fun canJoin(table: Table, client: Client): Boolean

}

class NotReserved: TableState {

    override fun canJoin(table: Table, client: Client): Boolean {
        throw TableNotReserved(client.id(), table.id())
    }

}

internal class ReservedRouletteTable: TableState {

    private val basicJoiningRules = BasicJoiningRules(MAX_ROULETTE_PARTICIPANTS_COUNT)

    override fun canJoin(table: Table, client: Client): Boolean {
        basicJoiningRules.canJoin(table, client)
        return true
    }

    companion object {
        const val MAX_ROULETTE_PARTICIPANTS_COUNT = 10
    }

}

internal class ReservedPokerTable(val initialBidingRate: Tokens): TableState {

    private val basicJoiningRules = BasicJoiningRules(ReservedRouletteTable.MAX_ROULETTE_PARTICIPANTS_COUNT)

    override fun canJoin(table: Table, client: Client): Boolean {
        basicJoiningRules.canJoin(table, client)
        if(client.tokens() < initialBidingRate) throw InitialBidingRateTooHigh(client.id(), client.tokens(), initialBidingRate)
        return true
    }

    companion object {
        const val MAX_POKER_PARTICIPANTS_COUNT = 10
    }

}

internal class Closed: TableState {

    override fun canJoin(table: Table, client: Client): Boolean {
        return false
    }

}

internal class BasicJoiningRules(val maxParticipantCount: Int) {

    fun canJoin(table: Table, client: Client): Boolean {
        val clientId = client.id()
        if (!table.isReserved()) throw TableNotReserved(client.id(), table.id())
        if (table.hasParticipation(Participation(clientId))) throw ClientAlreadyParticipated(clientId, table.id())
        if (client.doesParticipateToAnyTable()) throw ClientBusy(clientId)
        if(table.participation().count() >= ReservedPokerTable.MAX_POKER_PARTICIPANTS_COUNT) throw TableFull(client.id(), table.id(), maxParticipantCount)
        return true
    }

}

