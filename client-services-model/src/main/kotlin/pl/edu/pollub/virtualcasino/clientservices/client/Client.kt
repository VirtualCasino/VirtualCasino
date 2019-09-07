package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft
import java.lang.RuntimeException

class Client(private val id: ClientId = ClientId(),
             changes: MutableList<DomainEvent> = mutableListOf(),
             private val tableRepository: TableRepository
): EventSourcedAggregateRoot() {

    private var tokens = Tokens()

    init {
        changes.toMutableList().fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: BuyTokens) {
        if(command.tokens <= Tokens()) throw TokensCountMustBePositive(id, command.tokens)
        if(doesParticipateToAnyTable()) throw ClientBusy(id)
        `when`(TokensBought(clientId = id, tokens = command.tokens))
    }

    fun id(): ClientId = id

    fun `when`(event: RouletteGameLeft): Client {
        tokens = Tokens(event.playerTokens.count)
        changes.add(event)
        return this
    }

    fun tokens(): Tokens = Tokens(tokens.count)

    internal fun doesParticipateToAnyTable(): Boolean = tableRepository.containsWithParticipation(Participation(id))

    private fun patternMatch(event: DomainEvent): Client = when(event) {
        is TokensBought -> `when`(event)
        is RouletteGameLeft -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Client")
    }

    private fun `when`(event: TokensBought): Client {
        tokens += event.tokens
        changes.add(event)
        return this
    }

}