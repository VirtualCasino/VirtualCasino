package pl.edu.pollub.virtualcasino

import java.util.*

interface EventSourcedAggregateRootId {

    fun value(): UUID

}