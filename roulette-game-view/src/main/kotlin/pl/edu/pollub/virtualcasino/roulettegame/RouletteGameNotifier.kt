package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent

@Component
class RouletteGameNotifier(private val sender: SimpMessageSendingOperations) {

    fun notifyThat(event: DomainEvent) {
        sender.convertAndSend("/game/${event.aggregateId()}", event)
    }

}
