package pl.edu.pollub.virtualcasino.clientservices.table.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.events.JoinedTable

class FakedJoinedTableListener implements DomainEventListener<JoinedTable> {

    List<JoinedTable> listenedEvents = []

    @Override
    boolean isListenFor(@NotNull DomainEvent event) {
        return event.type() == JoinedTable.TYPE
    }

    @Override
    void reactTo(@NotNull DomainEvent event) {
        listenedEvents.add((JoinedTable) event)
    }
}
