package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.roulettegame.commands.LeaveRouletteGame
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.RoulettePlayerNotExist
import java.lang.RuntimeException

class RouletteGame(private val id: RouletteGameId = RouletteGameId(),
                   private val changes: MutableList<DomainEvent> = mutableListOf(),
                   private val eventPublisher: RouletteGameEventPublisher
): EventSourcedAggregateRoot(changes) {

    private val players = mutableListOf<RoulettePlayer>()

    init {
        changes.toMutableList().fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: LeaveRouletteGame) {
        val playerId = command.playerId
        val player = players.find { it.id() == playerId } ?: throw RoulettePlayerNotExist(id(), playerId)
        val event = RouletteGameLeft(gameId = id(), playerId = player.id(), playerTokens = player.tokens())
        `when`(event)
        eventPublisher.publish(event)
    }

    fun id(): RouletteGameId = id

    fun players(): List<RoulettePlayer> = players

    override fun patternMatch(event: DomainEvent): RouletteGame  = when(event) {
        is RouletteTableReserved -> `when`(event)
        is JoinedTable -> `when`(event)
        is RouletteGameLeft -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for RouletteGame")
    }

    fun `when`(event: JoinedTable): RouletteGame {
        players.add(RoulettePlayer(event.clientId, event.clientTokens))
        changes.add(event)
        return this
    }

    fun `when`(event: RouletteTableReserved): RouletteGame {
        players.add(RoulettePlayer(event.clientId, event.clientTokens))
        changes.add(event)
        return this
    }

    private fun `when`(event: RouletteGameLeft): RouletteGame {
        players.removeIf { it.id() == event.playerId }
        changes.add(event)
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