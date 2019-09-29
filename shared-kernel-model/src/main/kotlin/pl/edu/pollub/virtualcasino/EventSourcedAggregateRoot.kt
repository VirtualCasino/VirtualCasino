package pl.edu.pollub.virtualcasino

abstract class EventSourcedAggregateRoot<I : EventSourcedAggregateRootId>(open val id: I) {

    private val changes: MutableList<DomainEvent> = mutableListOf()

    abstract fun id(): I

    fun getUncommittedChanges(): List<DomainEvent> = changes

    fun markChangesAsCommitted() = changes.clear()

    fun applyChange(change: DomainEvent) {
        changes.add(change)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventSourcedAggregateRoot<*>) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}