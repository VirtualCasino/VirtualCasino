package pl.edu.pollub.virtualcasino

abstract class EventSourcedAggregateRoot {

    protected val changes: MutableList<DomainEvent> = mutableListOf()

    fun getUncommittedChanges(): List<DomainEvent> = changes

    fun markChangesAsCommitted() = changes.clear()

}