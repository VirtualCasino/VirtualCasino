package pl.edu.pollub.virtualcasino.clientservices.domain.table

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.events.TableReserved
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.table.ParticipantsOfTable
import java.lang.IllegalStateException
import java.util.UUID.randomUUID

class Table(val id: TableId = TableId(),
            private val changes: MutableList<DomainEvent> = mutableListOf()
) {

    private val participation = mutableListOf<Participation>()

    init {
        changes.fold(this) { _, event -> patternMatch(event) }
        markChangesAsCommitted()
    }

    fun handle(command: ReserveTable) {
        val event = TableReserved(tableId = id, clientId = command.clientId)
        `when`(event)
        changes.add(event)
    }

    fun hasParticipation(participation: Participation): Boolean = this.participation.contains(participation)

    fun participantsOfTable(): ParticipantsOfTable = ParticipantsOfTable(id, participation)

    private fun `when`(event: TableReserved): Table {
        participation.add(Participation(event.clientId))
        return this
    }

    private fun patternMatch(event: DomainEvent): Table = when(event) {
        is TableReserved -> `when`(event)
        else -> throw IllegalStateException("event: $event is not acceptable for table")
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