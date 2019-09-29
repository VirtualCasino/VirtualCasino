package pl.edu.pollub.virtualcasino.clientservices.table

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.table.projection.TableParticipants
import pl.edu.pollub.virtualcasino.eventstore.EventSourcedMongoRepository

@Component
class EventSourcedMongoTableRepository(val factory: TableFactory,
                                       val clientServicesBoundedContextMongoTemplate: MongoTemplate,
                                       objectMapper: ObjectMapper
): EventSourcedMongoRepository<Table, TableId>(clientServicesBoundedContextMongoTemplate, objectMapper),  TableRepository {

    override fun createAggregate(id: TableId, events: List<DomainEvent>): Table = factory.create(id, events)

    override fun persistProjections(aggregate: Table) {
        saveParticipantsProjection(aggregate)
    }

    override fun containsWithParticipation(participation: Participation): Boolean {
        val query = Query()
        query.addCriteria(Criteria.where("participation.clientId").`is`(participation.clientId))
        val projection = clientServicesBoundedContextMongoTemplate.findOne(query, TableParticipants::class.java)
        return projection != null
    }

    private fun saveParticipantsProjection(aggregate: Table) {
        val query = Query()
        query.addCriteria(Criteria.where("tableId").isEqualTo(aggregate.id()))
        val projection = clientServicesBoundedContextMongoTemplate.findOne(query, TableParticipants::class.java) ?: TableParticipants(tableId = aggregate.id)
        projection.participation = aggregate.participation()
        clientServicesBoundedContextMongoTemplate.save(projection)
    }
}