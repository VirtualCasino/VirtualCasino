package pl.edu.pollub.virtualcasino.clientservices.domain.client

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.domain.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import java.lang.RuntimeException
import java.util.UUID.randomUUID

class Client(val id: ClientId = ClientId(),
             private val changes: MutableList<DomainEvent> = mutableListOf(),
             private val tableRepository: TableRepository
) {

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

    private fun `when`(event: TokensBought): Client {
        tokens = tokens.changeCount(event.tokens)
        return this
    }

    private fun patternMatch(event: DomainEvent): Client = when(event) {
        is TokensBought -> `when`(event)
        else -> throw RuntimeException("event: $event is not acceptable for Client")
    }

    fun getUncommittedChanges(): List<DomainEvent> = changes

    fun markChangesAsCommitted() = changes.clear()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Client

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}

data class ClientId(val value: String = randomUUID().toString())

data class Tokens(val count: Int = 0) {

    fun changeCount(tokens: Tokens = Tokens()): Tokens = Tokens(this.count + tokens.count)

    operator fun compareTo(other: Tokens): Int = count - other.count

}