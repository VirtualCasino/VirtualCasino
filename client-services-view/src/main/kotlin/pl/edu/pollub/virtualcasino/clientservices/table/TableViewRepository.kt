package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import java.util.*

@Component
class TableViewRepository(private val clientServicesViewMongoTemplate: MongoTemplate) {

    fun find(aggregateId: UUID): TableView? {
        val query = Query()
        query.addCriteria(Criteria.where("tableViewId").isEqualTo(aggregateId))
        return clientServicesViewMongoTemplate.findOne(query, TableView::class.java)
    }

    fun save(tableView: TableView) {
        clientServicesViewMongoTemplate.save(tableView)
    }

    fun findAll(): List<TableView> = clientServicesViewMongoTemplate.findAll(TableView::class.java)

}