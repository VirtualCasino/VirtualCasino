package pl.edu.pollub.virtualcasino.client

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.client.events.TokensBought
import pl.edu.pollub.virtualcasino.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.table.Participation
import pl.edu.pollub.virtualcasino.table.TableRepository
import java.lang.RuntimeException

class Client(val id: ClientId = ClientId(),
             private val tableRepository: TableRepository,
             private val changes: MutableList<DomainEvent> = mutableListOf()
): EventSourcedAggregateRoot(changes) {

    private var tokens = Tokens()

    init {
        changes.fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: BuyTokens) {
        if(command.tokens <= Tokens()) throw TokensCountMustBePositive(id, command.tokens)
        if(doesParticipateToAnyTable()) throw ClientBusy(id)
        val event = TokensBought(clientId = command.clientId, tokens = command.tokens)
        `when`(event)
        changes.add(event)
    }

    fun tokens(): Tokens = tokens

    fun doesParticipateToAnyTable(): Boolean = tableRepository.containsWithParticipation(Participation(id))

    override fun patternMatch(event: DomainEvent): Client = when(event) {
        is TokensBought -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Client")
    }

    private fun `when`(event: TokensBought): Client {
        tokens = tokens.changeCount(event.tokens)
        return this
    }

}