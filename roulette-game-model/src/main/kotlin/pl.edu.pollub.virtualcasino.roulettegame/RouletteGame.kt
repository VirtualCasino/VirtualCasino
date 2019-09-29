package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.roulettegame.commands.*
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetCanceled
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteBetPlaced
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinStarted
import pl.edu.pollub.virtualcasino.roulettegame.events.SpinFinished
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.*
import java.lang.RuntimeException
import java.time.Clock

class RouletteGame(override val id: RouletteGameId = RouletteGameId(),
                   changes: MutableList<DomainEvent> = mutableListOf(),
                   private val croupier: RouletteCroupier,
                   private val eventPublisher: RouletteGameEventPublisher,
                   private val clock: Clock
): EventSourcedAggregateRoot<RouletteGameId>(id) {

    private val players = mutableSetOf<RoulettePlayer>()
    private var state: RouletteGameState = SpinNotStartYet()

    init {
        changes.toMutableList().fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: StartSpin) {
        if(!state.isSpinFinished()) throw CurrentSpinNotFinished(id)
        val currentTime = clock.instant()
        if(command.bettingTimeEnd < currentTime) throw BettingTimeEndMustBeFuture(id, command.bettingTimeEnd, currentTime)
        `when`(SpinStarted(gameId = id, bettingTimeEnd = command.bettingTimeEnd))
        croupier.scheduleTheFinishOfSpinForGame(id)
    }

    fun handle(command: PlaceRouletteBet) {
        val betValue = command.value
        val playerId = command.playerId
        val player = players.find { it.id() == playerId } ?: throw RoulettePlayerNotExist(id, playerId)
        validateBettingTime(player)
        if(betValue <= Tokens()) throw BetValueMustBePositive(id, player.id(), betValue)
        val playerFreeTokens = player.freeTokens()
        if(betValue > playerFreeTokens) throw PlacedBetsExceedPlayerFreeTokens(id, player.id(), betValue, playerFreeTokens)
        `when`(RouletteBetPlaced(gameId = id, playerId = player.id(), field = command.field, value = command.value))
    }

    fun handle(command: CancelRouletteBet) {
        val playerId = command.playerId
        val player = players.find { it.id() == playerId } ?: throw RoulettePlayerNotExist(id, playerId)
        validateBettingTime(player)
        val canceledBetField = command.field
        if(!player.placedBetsFields().contains(canceledBetField)) throw BetNotExist(id, playerId, canceledBetField)
        `when`(RouletteBetCanceled(gameId = id, playerId = playerId, field = canceledBetField))
    }

    fun handle(command: FinishSpin) {
        if(!state.isSpinStarted()) throw SpinNotStartedYet(id)
        if(state.isSpinFinished()) throw SpinAlreadyFinished(id)
        if(!state.isBettingTimeEnded()) throw BettingTimeNotEndedYet(id)
        `when`(SpinFinished(gameId = id, fieldDrawn = command.fieldDrawn))
        if(players.isNotEmpty()) croupier.scheduleTheStartOfSpinForGame(id)
    }

    fun handle(command: LeaveRouletteGame) {
        val playerId = command.playerId
        val player = players.find { it.id() == playerId } ?: throw RoulettePlayerNotExist(id, playerId)
        val playerTokensAfterLeftGame = when(state.isBettingTimeEnded()) {
            true -> player.freeTokens()
            else -> player.tokens()
        }
        val event = RouletteGameLeft(gameId = id, playerId = player.id(), playerTokens = playerTokensAfterLeftGame)
        `when`(event)
        eventPublisher.publish(event)
    }

    fun players(): Set<RoulettePlayer> = players

    override fun id(): RouletteGameId = id

    fun `when`(event: JoinedTable): RouletteGame {
        players.add(RoulettePlayer(event.clientId, event.clientTokens))
        applyChange(event)
        return this
    }

    fun `when`(event: RouletteTableReserved): RouletteGame {
        players.add(RoulettePlayer(event.clientId, event.clientTokens))
        applyChange(event)
        return this
    }

    private fun validateBettingTime(player: RoulettePlayer) {
        if(!state.isSpinStarted()) throw SpinNotStartedYet(id)
        if(state.isBettingTimeEnded()) throw BettingTimeExceeded(id, player.id())
    }

    private fun `when`(event: SpinStarted): RouletteGame {
        state = SpinStarted(clock, event.bettingTimeEnd)
        applyChange(event)
        return this
    }

    private fun `when`(event: RouletteBetPlaced): RouletteGame {
        val playerThatPlacedBet = players.find { it.id() == event.playerId }!!
        playerThatPlacedBet.placeBet(event.field, event.value)
        applyChange(event)
        return this
    }

    private fun `when`(event: RouletteBetCanceled): RouletteGame {
        val playerThatPlacedBet = players.find { it.id() == event.playerId }!!
        playerThatPlacedBet.cancelBet(event.field)
        applyChange(event)
        return this
    }

    private fun `when`(event: SpinFinished): RouletteGame {
        players.forEach { it.chargeForBets(event.fieldDrawn) }
        state = SpinFinished()
        applyChange(event)
        return this
    }

    private fun `when`(event: RouletteGameLeft): RouletteGame {
        players.removeIf { it.id() == event.playerId }
        applyChange(event)
        return this
    }

    private fun patternMatch(event: DomainEvent): RouletteGame  = when(event) {
        is RouletteTableReserved -> `when`(event)
        is JoinedTable -> `when`(event)
        is RouletteGameLeft -> `when`(event)
        is RouletteBetPlaced -> `when`(event)
        is RouletteBetCanceled -> `when`(event)
        is SpinStarted -> `when`(event)
        is SpinFinished -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for RouletteGame")
    }

}