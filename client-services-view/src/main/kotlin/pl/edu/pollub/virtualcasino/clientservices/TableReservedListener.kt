package pl.edu.pollub.virtualcasino.clientservices

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved
import java.util.*

@RestController
@RequestMapping("/virtual-casino/view/casino-services/tables")
class TableViewApi(private val repository: TableViewRepository) {

    @GetMapping("/roulette")
    fun getTables(): List<TableView> {
        return repository.findAll()
    }

}

@Component
class TableReservedListener(private val repository: TableViewRepository) : DomainEventListener<RouletteTableReserved> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteTableReserved)

    }

    private fun reactTo(event: RouletteTableReserved) {
        val tableView = TableView(
                tableViewId = event.aggregateId(),
                playersCount = 1,
                maxPlayersCount = event.gameType().maxPlayers
        )
        repository.save(tableView)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteTableReserved

}


interface TableViewRepository : MongoRepository<TableView, UUID>

class TableView(@Id private val id: UUID = UUID.randomUUID(),
                private val tableViewId: UUID,
                private val playersCount: Int,
                private val maxPlayersCount: Int)
