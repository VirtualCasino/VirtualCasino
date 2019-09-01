package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.context.annotation.Configuration
import pl.edu.pollub.virtualcasino.clientservices.table.RouletteGameLeftListener
import javax.annotation.PostConstruct

@Configuration
class RouletteGameEventsSubscription(
        private val publisher: RouletteGameEventPublisher,
        private val rouletteGameLeftListener: RouletteGameLeftListener
) {

    @PostConstruct
    fun subscribeListeners() {
        publisher.subscribe(rouletteGameLeftListener)
    }
}