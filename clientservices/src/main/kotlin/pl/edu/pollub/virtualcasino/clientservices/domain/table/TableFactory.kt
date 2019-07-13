package pl.edu.pollub.virtualcasino.clientservices.domain.table

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent

@Component
class TableFactory {

    fun create(aggregateId: TableId = TableId(), events: List<DomainEvent> = emptyList()): Table =
            Table(aggregateId, events.toMutableList())

}