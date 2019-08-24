package pl.edu.pollub.virtualcasino.clientservices.client.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.client.ClientEventPublisher

class FakedClientEventPublisher implements ClientEventPublisher {

    List<DomainEventListener> listeners = []

    @Override
    void subscribe(@NotNull DomainEventListener listener) {
        listeners.add(listener)
    }

    @Override
    void unsubscribe(@NotNull DomainEventListener listener) {
        listeners.remove(listener)
    }

    @Override
    void publish(@NotNull DomainEvent event) {
        for(listener in listeners) {
            if(listener.isListenFor(event)) {
                listener.reactTo(event)
            }
        }
    }

}
