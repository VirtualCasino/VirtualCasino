package pl.edu.pollub.virtualcasino.clientservices.domain.client

interface ClientRepository {

    fun add(aggregate: Client): Boolean

    fun find(aggregateId: ClientId): Client?

    fun clear()

}