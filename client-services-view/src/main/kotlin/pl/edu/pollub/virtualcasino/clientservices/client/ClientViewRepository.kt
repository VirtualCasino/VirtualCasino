package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClientViewRepository(private val clientServicesViewMongoTemplate: MongoTemplate) {

    fun find(aggregateId: UUID): ClientView? {
        val query = Query()
        query.addCriteria(Criteria.where("clientViewId").isEqualTo(aggregateId))
        return clientServicesViewMongoTemplate.findOne(query, ClientView::class.java)
    }

    fun save(clientView: ClientView) {
        clientServicesViewMongoTemplate.save(clientView)
    }

}