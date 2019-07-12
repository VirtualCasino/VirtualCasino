package pl.edu.pollub.virtualcasino.clientservices.domain.client

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import java.util.UUID.randomUUID

class Client(val id: ClientId = ClientId(),
             private val changes: MutableList<DomainEvent> = mutableListOf(),
             private val tableRepository: TableRepository
) {

    fun isBusy(): Boolean = tableRepository.containsWithParticipation(Participation(id))

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