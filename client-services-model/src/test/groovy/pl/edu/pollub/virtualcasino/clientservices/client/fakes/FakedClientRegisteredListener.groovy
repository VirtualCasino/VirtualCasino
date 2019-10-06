package pl.edu.pollub.virtualcasino.clientservices.client.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.client.events.ClientRegistered

class FakedClientRegisteredListener implements DomainEventListener<ClientRegistered> {

    List<ClientRegistered> listenedEvents = []

    @Override
    boolean isListenFor(@NotNull DomainEvent event) {
        return event.type() == ClientRegistered.TYPE
    }

    @Override
    void reactTo(@NotNull DomainEvent event) {
        listenedEvents.add((ClientRegistered) event)
    }
}
