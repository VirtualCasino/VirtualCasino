package pl.edu.pollub.virtualcasino

abstract class EventSourcedAggregateRoot(private val changes: MutableList<DomainEvent> = mutableListOf()) {

    abstract fun patternMatch(event: DomainEvent): EventSourcedAggregateRoot

    fun getUncommittedChanges(): List<DomainEvent> = changes

    fun markChangesAsCommitted() = changes.clear()

}