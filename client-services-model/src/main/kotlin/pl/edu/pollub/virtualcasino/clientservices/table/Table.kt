package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotExist
import pl.edu.pollub.virtualcasino.clientservices.table.commands.JoinTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReserveRouletteTable
import pl.edu.pollub.virtualcasino.clientservices.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import java.lang.RuntimeException

class Table(override val id: TableId = TableId(),
            changes: MutableList<DomainEvent> = mutableListOf(),
            private val clientRepository: ClientRepository,
            private val eventPublisher: TableEventPublisher
): EventSourcedAggregateRoot<TableId>(id) {

    private var state: TableState = NotReserved()
    private val participation = mutableSetOf<Participation>()
    private val reservationRules = BasicReservationRules()
    private val initialBiddingRateRules = InitialBiddingRateRules()

    init {
        changes.toMutableList().fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: ReserveRouletteTable) {
        val clientId = command.clientId()
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        reservationRules.canReserve(this, client)
        val event = RouletteTableReserved(tableId = id, clientId = clientId, clientTokens = client.tokens(), firstPlayerNick = client.nick())
        `when`(event)
        eventPublisher.publish(event)
    }

    fun handle(command: ReservePokerTable) {
        val clientId = command.clientId()
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        reservationRules.canReserve(this, client)
        initialBiddingRateRules.isInitialBiddingRateValid(command, client)
        val event = PokerTableReserved(tableId = id, clientId = clientId, initialBidingRate = command.initialBidingRate, clientTokens = client.tokens())
        `when`(event)
        eventPublisher.publish(event)
    }

    fun handle(command: JoinTable) {
        val clientId = command.clientId
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        canJoin(client)
        val event = JoinedTable(tableId = id, clientId = clientId, clientTokens = client.tokens())
        `when`(event)
        eventPublisher.publish(event)
    }

    override fun id(): TableId = id

    fun participation(): Set<Participation> = participation

    fun `when`(event: RouletteGameLeft): Table {
        participation.removeIf { it.clientId == ClientId(event.playerId) }
        if(participation.isEmpty()) {
            state = Closed()
        }
        applyChange(event)
        return this
    }

    internal fun hasParticipation(participation: Participation): Boolean = this.participation.contains(participation)
    
    internal fun isReserved() = state.isReserved()

    internal fun isClosed() = state.isClosed()

    private fun canJoin(client: Client): Boolean = state.canJoin(this, client)

    private fun `when`(event: RouletteTableReserved): Table {
        participation.add(Participation(event.clientId))
        state = ReservedRouletteTable()
        applyChange(event)
        return this
    }

    private fun `when`(event: PokerTableReserved): Table {
        participation.add(Participation(event.clientId))
        state = ReservedPokerTable(event.initialBidingRate)
        applyChange(event)
        return this
    }

    private fun `when`(event: JoinedTable): Table {
        participation.add(Participation(event.clientId))
        applyChange(event)
        return this
    }

    private fun patternMatch(event: DomainEvent): Table = when(event) {
        is RouletteTableReserved -> `when`(event)
        is PokerTableReserved -> `when`(event)
        is JoinedTable -> `when`(event)
        is RouletteGameLeft -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Table")
    }

}

data class Participation(val clientId: ClientId)