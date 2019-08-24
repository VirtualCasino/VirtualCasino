package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.DomainEvent

interface TableFactory {

    fun create(aggregateId: TableId = TableId(), events: List<DomainEvent> = emptyList()): Table

}