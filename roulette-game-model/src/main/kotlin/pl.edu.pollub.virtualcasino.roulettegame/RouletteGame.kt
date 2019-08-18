package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.RouletteTableReserved
import java.lang.RuntimeException
import java.util.*

class RouletteGame(val id: RouletteGameId = RouletteGameId(),
                   private val changes: MutableList<DomainEvent> = mutableListOf()
): EventSourcedAggregateRoot(changes) {

    private val players = mutableListOf<RoulettePlayer>()

    init {
        changes.fold(this) { _, event -> patternMatch(event) }
    }

    internal fun players(): List<RoulettePlayer> = players

    override fun patternMatch(event: DomainEvent): RouletteGame  = when(event) {
        is RouletteTableReserved -> `when`(event)
        is JoinedTable -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for RouletteGame")
    }

    fun `when`(event: JoinedTable): RouletteGame {
        players.add(RoulettePlayer(event.clientId))
        return this
    }

    private fun `when`(event: RouletteTableReserved): RouletteGame {
        players.add(RoulettePlayer(event.clientId))
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RouletteGame

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

data class RouletteGameId(val value: UUID = UUID.randomUUID()) {

    constructor(tableId: TableId) : this(tableId.value)

}