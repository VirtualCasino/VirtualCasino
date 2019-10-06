package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.table.events.GameType.POKER
import pl.edu.pollub.virtualcasino.clientservices.table.events.GameType.ROULETTE
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.*

internal interface TableState {

    fun canJoin(table: Table, client: Client): Boolean

    fun isReserved(): Boolean

    fun isClosed(): Boolean

}

internal class NotReserved: TableState {

    override fun canJoin(table: Table, client: Client): Boolean {
        throw TableNotReserved(client.id(), table.id())
    }

    override fun isReserved(): Boolean = false

    override fun isClosed(): Boolean = false

}

internal class ReservedRouletteTable: TableState {

    private val gameType = ROULETTE
    private val basicJoiningRules = BasicJoiningRules(gameType.maxPlayers)

    override fun canJoin(table: Table, client: Client): Boolean {
        basicJoiningRules.canJoin(table, client)
        return true
    }

    override fun isReserved(): Boolean = true

    override fun isClosed(): Boolean = false

}

internal class ReservedPokerTable(val initialBidingRate: Tokens): TableState {

    private val gameType = POKER
    private val basicJoiningRules = BasicJoiningRules(gameType.maxPlayers)

    override fun canJoin(table: Table, client: Client): Boolean {
        basicJoiningRules.canJoin(table, client)
        if(client.tokens() < initialBidingRate) throw InitialBidingRateTooHigh(client.id(), client.tokens(), initialBidingRate)
        return true
    }

    override fun isReserved(): Boolean = true

    override fun isClosed(): Boolean = false

}

internal class Closed: TableState {

    override fun canJoin(table: Table, client: Client): Boolean {
        throw TableClosed(client.id(), table.id())
    }

    override fun isReserved(): Boolean = false

    override fun isClosed(): Boolean = true

}

internal class BasicJoiningRules(private val maxParticipantCount: Int) {

    fun canJoin(table: Table, client: Client): Boolean {
        val clientId = client.id()
        if (!table.isReserved()) throw TableNotReserved(client.id(), table.id())
        if (table.isClosed()) throw TableClosed(clientId, table.id())
        if (table.hasParticipation(Participation(clientId))) throw ClientAlreadyParticipated(clientId, table.id())
        if (client.doesParticipateToAnyTable()) throw ClientBusy(clientId)
        if(table.participation().count() >= maxParticipantCount) throw TableFull(client.id(), table.id(), maxParticipantCount)
        return true
    }

}

