package pl.edu.pollub.virtualcasino.table

interface TableRepository {

    fun add(aggregate: Table): Boolean

    fun find(id: TableId): Table?

    fun clear()

    fun containsWithParticipation(participation: Participation): Boolean

}