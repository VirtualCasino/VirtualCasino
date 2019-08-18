package pl.edu.pollub.virtualcasino.clientservices.table.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.samples.events.PokerTableReserved

class FakedPokerTableReservedListener implements DomainEventListener<PokerTableReserved> {

    List<PokerTableReserved> listenedEvents = []

    @Override
    boolean isListenFor(@NotNull DomainEvent event) {
        return event.type() == PokerTableReserved.TYPE
    }

    @Override
    void reactTo(@NotNull DomainEvent event) {
        listenedEvents.add((PokerTableReserved) event)
    }
}
