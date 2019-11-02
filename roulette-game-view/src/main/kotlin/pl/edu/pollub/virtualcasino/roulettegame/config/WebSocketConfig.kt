package pl.edu.pollub.virtualcasino.roulettegame.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArrayList


@Configuration
@EnableWebSocket
class WebSocketConfig: WebSocketConfigurer {

    @Bean
    fun socketSessions(): SocketSessions = SocketSessions()

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(socketSessions(), "/game")
    }

}

class SocketSessions: TextWebSocketHandler() {

    val sessions: CopyOnWriteArrayList<WebSocketSession> = CopyOnWriteArrayList()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        sessions.add(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        super.afterConnectionClosed(session, status)
        sessions.remove(session)
    }

    fun sendTextMessage(message: String) {
        sessions.forEach {
            it.sendMessage(TextMessage(message))
        }
    }
}