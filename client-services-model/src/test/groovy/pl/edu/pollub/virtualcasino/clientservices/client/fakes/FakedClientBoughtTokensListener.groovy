package pl.edu.pollub.virtualcasino.clientservices.client.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.client.events.TokensBought

class FakedTokensBoughtListener implements DomainEventListener<TokensBought> {

    List<TokensBought> listenedEvents = []

    @Override
    boolean isListenFor(@NotNull DomainEvent event) {
        return event.type() == TokensBought.TYPE
    }

    @Override
    void reactTo(@NotNull DomainEvent event) {
        listenedEvents.add((TokensBought) event)
    }
}
