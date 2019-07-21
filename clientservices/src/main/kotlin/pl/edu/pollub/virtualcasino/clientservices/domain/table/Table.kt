package pl.edu.pollub.virtualcasino.clientservices.domain.table

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Client
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientNotExist
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType.*
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.JoinToTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.TableReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.*
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.table.ParticipantsOfTable
import java.lang.RuntimeException
import java.util.UUID.randomUUID

class Table(val id: TableId = TableId(),
            private val changes: MutableList<DomainEvent> = mutableListOf(),
            private val requirementsFactory: TableRequirementsFactory,
            private val clientRepository: ClientRepository
) {

    private val participation = mutableListOf<Participation>()
    private var requirements: TableRequirements = NoRequirements()

    init {
        changes.fold(this) { _, event -> patternMatch(event) }
        markChangesAsCommitted()
    }

    fun handle(command: ReserveTable) {
        if(isReserved()) throw TableAlreadyReserved(command.clientId, id, participation.first().clientId)
        val client = clientRepository.find(command.clientId) ?: throw ClientNotExist(command.clientId)
        if(client.isBusy()) throw ClientBusy(command.clientId)
        if(command.gameType == POKER) validatePokerInitialBiddingRate(command, client)
        val event = TableReserved(tableId = id, clientId = command.clientId, gameType = command.gameType, initialBidingRate = command.initialBidingRate)
        `when`(event)
        changes.add(event)
    }

    fun handle(command: JoinToTable) {
        if(!isReserved()) throw TableNotReserved(command.clientId, id)
        if(hasParticipation(Participation(command.clientId))) throw ClientAlreadyParticipated(command.clientId, id)
        val client = clientRepository.find(command.clientId) ?: throw ClientNotExist(command.clientId)
        if(client.isBusy()) throw ClientBusy(command.clientId)
        requirements.check(client)
        val event = JoinedTable(tableId = id, clientId = command.clientId)
        `when`(event)
        changes.add(event)
    }

    fun hasParticipation(participation: Participation): Boolean = this.participation.contains(participation)

    fun participantsOfTable(): ParticipantsOfTable = ParticipantsOfTable(id, participation)

    private fun isReserved() = !participation.isEmpty()

    private fun validatePokerInitialBiddingRate(command: ReserveTable, client: Client) {
        if (command.initialBidingRate <= Tokens(0)) throw InitialBidingRateMustBePositive(command.clientId, id, command.initialBidingRate)
        if (client.tokens() < command.initialBidingRate) throw InitialBidingRateTooHigh(command.clientId, id, client.tokens(), command.initialBidingRate)
    }

    private fun `when`(event: TableReserved): Table {
        participation.add(Participation(event.clientId))
        requirements = requirementsFactory.create(event, participation)
        return this
    }

    private fun `when`(event: JoinedTable): Table {
        participation.add(Participation(event.clientId))
        return this
    }

    private fun patternMatch(event: DomainEvent): Table = when(event) {
        is TableReserved -> `when`(event)
        is JoinedTable -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Table")
    }

    fun getUncommittedChanges(): List<DomainEvent> = changes

    fun markChangesAsCommitted() = changes.clear()

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

data class TableId(val value: String = randomUUID().toString())

data class Participation(val clientId: ClientId)