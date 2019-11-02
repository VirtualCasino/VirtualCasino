package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.context.annotation.Configuration
import pl.edu.pollub.virtualcasino.clientservices.table.RouletteGameLeftListener
import javax.annotation.PostConstruct

@Configuration
class RouletteGameEventsSubscription(
        private val publisher: RouletteGameEventPublisher,
        private val rouletteGameLeftListener: RouletteGameLeftListener,
        private val viewRouletteGameBetCanceledListener: ViewRouletteGameBetCanceledListener,
        private val viewRouletteGameBetPlacedListener: ViewRouletteGameBetPlacedListener,
        private val viewRouletteGameLeftListener: ViewRouletteGameLeftListener,
        private val viewRouletteGameSpinFinishedListener: ViewRouletteGameSpinFinishedListener,
        private val viewRouletteGameSpinStartedListener: ViewRouletteGameSpinStartedListener,
        private val viewRouletteGameTableJoinedListener: ViewRouletteGameTableJoinedListener,
        private val viewRouletteGameTableReservedListener: ViewRouletteGameTableReservedListener
) {

    @PostConstruct
    fun subscribeListeners() {
        publisher.subscribe(rouletteGameLeftListener)
        publisher.subscribe(viewRouletteGameBetCanceledListener)
        publisher.subscribe(viewRouletteGameBetPlacedListener)
        publisher.subscribe(viewRouletteGameLeftListener)
        publisher.subscribe(viewRouletteGameSpinFinishedListener)
        publisher.subscribe(viewRouletteGameSpinStartedListener)
        publisher.subscribe(viewRouletteGameTableJoinedListener)
        publisher.subscribe(viewRouletteGameTableReservedListener)
    }
}