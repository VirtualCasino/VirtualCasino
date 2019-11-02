package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.context.annotation.Configuration
import pl.edu.pollub.virtualcasino.clientservices.client.ViewClientLeftRouletteGameListener
import pl.edu.pollub.virtualcasino.roulettegame.JoinedTableListener
import pl.edu.pollub.virtualcasino.roulettegame.RouletteTableReservedListener
import pl.edu.pollub.virtualcasino.roulettegame.ViewRouletteGameTableJoinedListener
import pl.edu.pollub.virtualcasino.roulettegame.ViewRouletteGameTableReservedListener
import javax.annotation.PostConstruct

@Configuration
class TableEventsSubscription(
        private val publisher: TableEventPublisher,
        private val joinedTableListener: JoinedTableListener,
        private val viewTableJoinedListener: ViewTableJoinedListener,
        private val rouletteTableReservedListener: RouletteTableReservedListener,
        private val tableReservedListener: ViewRouletteTableReservedListener,
        private val viewRouletteGameLeftListener: ViewRouletteTableLeftListener,
        private val viewClientLeftRouletteGameListener: ViewClientLeftRouletteGameListener,
        private val viewRouletteGameTableJoinedListener: ViewRouletteGameTableJoinedListener,
        private val viewRouletteGameTableReservedListener: ViewRouletteGameTableReservedListener,
        private val clientLeftGameLeftListener: RouletteGameLeftListener
) {

    @PostConstruct
    fun subscribeListeners() {
        publisher.subscribe(joinedTableListener)
        publisher.subscribe(viewTableJoinedListener)
        publisher.subscribe(rouletteTableReservedListener)
        publisher.subscribe(tableReservedListener)
        publisher.subscribe(viewRouletteGameLeftListener)
        publisher.subscribe(clientLeftGameLeftListener)
        publisher.subscribe(viewClientLeftRouletteGameListener)
        publisher.subscribe(viewRouletteGameTableJoinedListener)
        publisher.subscribe(viewRouletteGameTableReservedListener)
    }

}