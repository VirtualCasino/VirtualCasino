package pl.edu.pollub.virtualcasino

interface DomainEventPublisher {

    fun subscribe(listener: DomainEventListener<*>)

    fun unsubscribe(listener: DomainEventListener<*>)

    fun publish(event: DomainEvent)

}