package pl.edu.pollub.virtualcasino.clientservices.client

interface ClientRepository {

    fun add(aggregate: Client): Boolean

    fun find(id: ClientId): Client?

    fun clear()

}