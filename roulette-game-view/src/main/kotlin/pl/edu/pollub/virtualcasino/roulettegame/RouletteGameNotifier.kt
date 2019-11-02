package pl.edu.pollub.virtualcasino.roulettegame

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.roulettegame.config.SocketSessions

@Component
class RouletteGameNotifier(private val socketSessions: SocketSessions, private val clientObjectMapper: ObjectMapper) {

    fun notifyThat(event: DomainEvent) {
        socketSessions.sendTextMessage(clientObjectMapper.writeValueAsString(event))
    }

}
