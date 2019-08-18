package pl.edu.pollub.virtualcasino

interface DomainEventListener<E : DomainEvent> {

    fun isListenFor(event: DomainEvent): Boolean

    fun reactTo(event: DomainEvent)

}