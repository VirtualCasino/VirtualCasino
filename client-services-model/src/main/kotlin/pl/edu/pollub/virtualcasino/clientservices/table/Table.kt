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
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import java.lang.RuntimeException

class Table(private val id: TableId = TableId(),
            private val changes: MutableList<DomainEvent> = mutableListOf(),
            private val clientRepository: ClientRepository,
            private val eventPublisher: TableEventPublisher
): EventSourcedAggregateRoot(changes) {

    private var state: TableState = NotReserved()
    private val participation = mutableListOf<Participation>()
    private val reservationRules = BasicReservationRules()
    private val initialBiddingRateRules = InitialBiddingRateRules()

    init {
        changes.toMutableList().fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: ReserveRouletteTable) {
        val clientId = command.clientId()
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        reservationRules.canReserve(this, client)
        val event = RouletteTableReserved(tableId = id, clientId = clientId, clientTokens = client.tokens())
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

    fun id(): TableId = id

    fun hasParticipation(participation: Participation): Boolean = this.participation.contains(participation)

    fun participation(): List<Participation> = participation

    override fun patternMatch(event: DomainEvent): Table = when(event) {
        is RouletteTableReserved -> `when`(event)
        is PokerTableReserved -> `when`(event)
        is JoinedTable -> `when`(event)
        is RouletteGameLeft -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Table")
    }

    internal fun isReserved() = state.isReserved()

    internal fun isClosed() = state.isClosed()

    private fun canJoin(client: Client): Boolean = state.canJoin(this, client)

    private fun `when`(event: RouletteTableReserved): Table {
        participation.add(Participation(event.clientId))
        state = ReservedRouletteTable()
        changes.add(event)
        return this
    }

    private fun `when`(event: PokerTableReserved): Table {
        participation.add(Participation(event.clientId))
        state = ReservedPokerTable(event.initialBidingRate)
        changes.add(event)
        return this
    }

    private fun `when`(event: JoinedTable): Table {
        participation.add(Participation(event.clientId))
        changes.add(event)
        return this
    }

    fun `when`(event: RouletteGameLeft): Table {
        participation.removeIf { it.clientId == ClientId(event.playerId) }
        if(participation.isEmpty()) {
            state = Closed()
        }
        changes.add(event)
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