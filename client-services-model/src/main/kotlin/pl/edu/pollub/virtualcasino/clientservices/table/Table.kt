package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotExist
import pl.edu.pollub.virtualcasino.clientservices.table.commands.JoinToTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReserveRouletteTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReserveTable
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.*
import java.lang.RuntimeException

class Table(val id: TableId = TableId(),
            private val changes: MutableList<DomainEvent> = mutableListOf(),
            private val clientRepository: ClientRepository,
            private val eventPublisher: TableEventPublisher
): EventSourcedAggregateRoot(changes) {

    private val participation = mutableListOf<Participation>()
    private var requirements: TableRequirements = NoRequirements()

    init {
        changes.fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: ReserveRouletteTable) {
        val clientId = command.clientId()
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        validateReservation(command, client)
        val event = RouletteTableReserved(tableId = id, clientId = clientId)
        `when`(event)
        eventPublisher.publish(event)
        changes.add(event)
    }

    fun handle(command: ReservePokerTable) {
        val clientId = command.clientId()
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        validateReservation(command, client)
        validatePokerInitialBiddingRate(command, client)
        val event = PokerTableReserved(tableId = id, clientId = clientId, initialBidingRate = command.initialBidingRate)
        `when`(event)
        eventPublisher.publish(event)
        changes.add(event)
    }

    fun handle(command: JoinToTable) {
        val clientId = command.clientId
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        validateJoining(command, client)
        val event = JoinedTable(tableId = id, clientId = clientId)
        `when`(event)
        eventPublisher.publish(event)
        changes.add(event)
    }

    fun participation(): List<Participation> = participation

    override fun patternMatch(event: DomainEvent): Table = when(event) {
        is RouletteTableReserved -> `when`(event)
        is PokerTableReserved -> `when`(event)
        is JoinedTable -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Table")
    }

    private fun hasParticipation(participation: Participation): Boolean = this.participation.contains(participation)

    private fun validateReservation(command: ReserveTable, client: Client) {
        val clientId = command.clientId()
        if (isReserved()) throw TableAlreadyReserved(clientId, id, participation.first().clientId)
        if (client.doesParticipateToAnyTable()) throw ClientBusy(clientId)
    }

    private fun validatePokerInitialBiddingRate(command: ReservePokerTable, client: Client) {
        val clientId = command.clientId()
        if (command.initialBidingRate <= Tokens()) throw InitialBidingRateMustBePositive(clientId, id, command.initialBidingRate)
        if (client.tokens() < command.initialBidingRate) throw InitialBidingRateTooHigh(clientId, id, client.tokens(), command.initialBidingRate)
    }

    private fun validateJoining(command: JoinToTable, client: Client) {
        val clientId = command.clientId
        if (!isReserved()) throw TableNotReserved(clientId, id)
        if (hasParticipation(Participation(clientId))) throw ClientAlreadyParticipated(clientId, id)
        if (client.doesParticipateToAnyTable()) throw ClientBusy(clientId)
        requirements.check(client)
    }

    private fun isReserved() = !participation.isEmpty()

    private fun `when`(event: RouletteTableReserved): Table {
        participation.add(Participation(event.clientId))
        requirements = RouletteTableRequirements(event.tableId, participation)
        return this
    }

    private fun `when`(event: PokerTableReserved): Table {
        participation.add(Participation(event.clientId))
        requirements = PokerTableRequirements(event.tableId, participation, event.initialBidingRate)
        return this
    }

    private fun `when`(event: JoinedTable): Table {
        participation.add(Participation(event.clientId))
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Table

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

data class Participation(val clientId: ClientId)