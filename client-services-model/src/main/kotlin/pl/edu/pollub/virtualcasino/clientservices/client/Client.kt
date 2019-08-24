package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.EventSourcedAggregateRoot
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.samples.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository
import java.lang.RuntimeException

class Client(private val id: ClientId = ClientId(),
             private val changes: MutableList<DomainEvent> = mutableListOf(),
             private val tableRepository: TableRepository,
             private val eventPublisher: ClientEventPublisher
): EventSourcedAggregateRoot(changes) {

    private var tokens = Tokens()

    init {
        changes.toMutableList().fold(this) { _, event -> patternMatch(event) }
    }

    fun handle(command: BuyTokens) {
        if(command.tokens <= Tokens()) throw TokensCountMustBePositive(id, command.tokens)
        if(doesParticipateToAnyTable()) throw ClientBusy(id)
        val event = TokensBought(clientId = id, tokens = command.tokens)
        `when`(event)
        eventPublisher.publish(event)
    }

    fun id(): ClientId = id

    fun tokens(): Tokens = tokens

    fun doesParticipateToAnyTable(): Boolean = tableRepository.containsWithParticipation(Participation(id))

    override fun patternMatch(event: DomainEvent): Client = when(event) {
        is TokensBought -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Client")
    }

    private fun `when`(event: TokensBought): Client {
        tokens = tokens.changeCount(event.tokens)
        changes.add(event)
        return this
    }

}