package pl.edu.pollub.virtualcasino.clientservices.domain.client

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.commands.IncreaseTokensCount
import pl.edu.pollub.virtualcasino.clientservices.domain.client.events.TokensCountIncreased
import pl.edu.pollub.virtualcasino.clientservices.domain.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import java.lang.RuntimeException
import java.util.UUID.randomUUID

class Client(val id: ClientId = ClientId(),
             private val changes: MutableList<DomainEvent> = mutableListOf(),
             private val tableRepository: TableRepository
) {

    var tokens = Tokens()

    init {
        changes.fold(this) { _, event -> patternMatch(event) }
        markChangesAsCommitted()
    }

    fun handle(command: IncreaseTokensCount) {
        val event = TokensCountIncreased(clientId = command.clientId, tokens = command.tokens)
        `when`(event)
        changes.add(event)
    }

    fun tokens(): Tokens = tokens

    fun isBusy(): Boolean = tableRepository.containsWithParticipation(Participation(id))

    private fun `when`(event: TokensCountIncreased): Client {
        tokens = tokens.increase(event.tokens)
        return this
    }

    private fun patternMatch(event: DomainEvent): Client = when(event) {
        is TokensCountIncreased -> `when`(event)
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

    fun increase(tokens: Tokens = Tokens()): Tokens = Tokens(this.count + tokens.count)

    operator fun compareTo(other: Tokens): Int = count - other.count

}