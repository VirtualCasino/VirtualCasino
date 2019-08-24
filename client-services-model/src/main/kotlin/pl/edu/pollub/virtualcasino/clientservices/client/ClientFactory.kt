package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.DomainEvent

interface ClientFactory {

    fun create(aggregateId: ClientId = ClientId(), events: List<DomainEvent> = emptyList()): Client

}