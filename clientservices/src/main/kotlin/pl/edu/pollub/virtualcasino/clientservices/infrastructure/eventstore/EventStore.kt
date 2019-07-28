package pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component

@Component
class EventStore(@Qualifier("casinoServicesWriteTemplate") val mongo: MongoTemplate) {

    fun saveEvents(aggregateId: String, events: List<EventDescriptor>) {
        val eventStream = getEventsOfAggregate(aggregateId) ?: EventStream(aggregateId = aggregateId)
        eventStream.add(events)
        mongo.save(eventStream)
        mongo.insertAll(events)
    }

    fun getEventsOfAggregate(aggregateId: String): EventStream? {
        val query = Query()
        query.addCriteria(Criteria.where("aggregateId").isEqualTo(aggregateId))
        return mongo.findOne(query, EventStream::class.java)
    }

    fun clear() {
        mongo.remove(Query(), EventStream::class.java)
        mongo.remove(Query(), EventDescriptor::class.java)
    }

}