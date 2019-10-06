package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.context.annotation.Configuration
import pl.edu.pollub.virtualcasino.roulettegame.JoinedTableListener
import pl.edu.pollub.virtualcasino.roulettegame.RouletteTableReservedListener
import javax.annotation.PostConstruct

@Configuration
class TableEventsSubscription(
        private val publisher: TableEventPublisher,
        private val joinedTableListener: JoinedTableListener,
        private val viewTableJoinedListener: ViewTableJoinedListener,
        private val rouletteTableReservedListener: RouletteTableReservedListener,
        private val tableReservedListener: ViewRouletteTableReservedListener
) {

    @PostConstruct
    fun subscribeListeners() {
        publisher.subscribe(joinedTableListener)
        publisher.subscribe(viewTableJoinedListener)
        publisher.subscribe(rouletteTableReservedListener)
        publisher.subscribe(tableReservedListener)
    }

}