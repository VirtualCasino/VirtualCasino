package pl.edu.pollub.virtualcasino.clientservices.table.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved

class FakedRouletteTableReservedListener implements DomainEventListener<RouletteTableReserved> {

    List<RouletteTableReserved> listenedEvents = []

    @Override
    boolean isListenFor(@NotNull DomainEvent event) {
        return event.type() == RouletteTableReserved.TYPE
    }

    @Override
    void reactTo(@NotNull DomainEvent event) {
        listenedEvents.add((RouletteTableReserved) event)
    }
}
