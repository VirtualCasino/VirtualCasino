package pl.edu.pollub.virtualcasino.roulettegame.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft

class FakedRouletteGameLeftListener implements DomainEventListener<RouletteGameLeft> {

    List<RouletteGameLeft> listenedEvents = []

    @Override
    boolean isListenFor(@NotNull DomainEvent event) {
        return event.type() == RouletteGameLeft.TYPE
    }

    @Override
    void reactTo(@NotNull DomainEvent event) {
        listenedEvents.add((RouletteGameLeft) event)
    }
}
