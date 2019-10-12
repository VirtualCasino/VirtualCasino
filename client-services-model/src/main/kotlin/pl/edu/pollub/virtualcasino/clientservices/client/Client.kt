package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.commands.RegisterClient
import pl.edu.pollub.virtualcasino.clientservices.client.events.ClientRegistered
import pl.edu.pollub.virtualcasino.clientservices.client.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotRegistered
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import java.lang.RuntimeException

class Client(override val id: ClientId = ClientId(),
             changes: MutableList<DomainEvent> = mutableListOf(),
             private val tableRepository: TableRepository,
             private val eventPublisher: ClientEventPublisher
): EventSourcedAggregateRoot<ClientId>(id) {

    private var state: ClientState = NotRegistered()
    private var tokens = Tokens()

    init {
        changes.toMutableList().fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: RegisterClient) {
        val event = ClientRegistered(clientId = id, nick = command.nick, initialTokens = tokens)
        `when`(event)
        eventPublisher.publish(event)
    }

    fun handle(command: BuyTokens) {
        state.canBuyTokens(this)
        if(command.tokens <= Tokens()) throw TokensCountMustBePositive(id, command.tokens)
        if(doesParticipateToAnyTable()) throw ClientBusy(id)
        val event = TokensBought(clientId = id, tokens = command.tokens)
        `when`(event)
        eventPublisher.publish(event)
    }

    fun nick(): Nick = state.nick() ?: throw ClientNotRegistered(id)

    override fun id(): ClientId = id

    fun tokens(): Tokens = Tokens(tokens.count)

    fun `when`(event: RouletteGameLeft): Client {
        tokens = Tokens(event.playerTokens.count)
        applyChange(event)
        return this
    }

    internal fun doesParticipateToAnyTable(): Boolean = tableRepository.containsWithParticipation(Participation(id))

    internal fun canReserveTable(): Boolean = state.canReserveTable(this)

    internal fun canJoinTable(): Boolean = state.canJoinTable(this)

    private fun patternMatch(event: DomainEvent): Client = when(event) {
        is ClientRegistered -> `when`(event)
        is TokensBought -> `when`(event)
        is RouletteGameLeft -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Client")
    }

    private fun `when`(event: TokensBought): Client {
        tokens += event.tokens
        applyChange(event)
        return this
    }

    private fun `when`(event: ClientRegistered): Client {
        state = Registered(event.nick)
        applyChange(event)
        return this
    }

}