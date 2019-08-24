package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.context.annotation.Configuration
import pl.edu.pollub.virtualcasino.roulettegame.JoinedTableListener
import pl.edu.pollub.virtualcasino.roulettegame.RouletteTableReservedListener
import javax.annotation.PostConstruct

@Configuration
class TableEventPublisherConfiguration(
        private val publisher: TableEventPublisher,
        private val joinedTableListener: JoinedTableListener,
        private val rouletteTableReservedListener: RouletteTableReservedListener
) {

    @PostConstruct
    fun subscribeListeners() {
        publisher.subscribe(joinedTableListener)
        publisher.subscribe(rouletteTableReservedListener)
    }

}