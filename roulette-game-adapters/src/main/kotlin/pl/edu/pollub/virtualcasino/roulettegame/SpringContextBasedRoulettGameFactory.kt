package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent

@Component
class SpringContextBasedRouletteGameFactory(private val context: ApplicationContext): RouletteGameFactory {

    override fun create(aggregateId: RouletteGameId, events: List<DomainEvent>): RouletteGame {
        return RouletteGame(aggregateId, events.toMutableList())
    }

}