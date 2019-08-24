package pl.edu.pollub.virtualcasino.eventpublisher

import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager.*
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.DomainEventPublisher

abstract class LocalEventPublisher : DomainEventPublisher {

    private val listeners = mutableListOf<DomainEventListener<*>>()

    override fun subscribe(listener: DomainEventListener<*>) {
        listeners.add(listener)
    }

    override fun unsubscribe(listener: DomainEventListener<*>) {
        listeners.remove(listener)
    }

    override fun publish(event: DomainEvent) {
        if(isSynchronizationActive()) {

            registerSynchronization(
                    object : TransactionSynchronizationAdapter() {

                        override fun afterCommit() {
                            publishEvent(event)
                        }

                    }
            )

        }
        else if(!isActualTransactionActive()) {
            publishEvent(event)
        }
    }

    private fun publishEvent(event: DomainEvent) {
        runCatching {
            for(listener in listeners) {
                if(listener.isListenFor(event)) {
                    listener.reactTo(event)
                }
            }
        }
        .onFailure {
            //TODO add logger with event and information about failure
        }
    }
}