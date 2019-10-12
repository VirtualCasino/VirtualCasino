package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.BasicQuery
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Criteria.*
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Component
import java.util.*
import kotlin.text.Typography.dollar

@Component
class TableViewRepository(private val clientServicesViewMongoTemplate: MongoTemplate) {

    fun find(aggregateId: UUID): TableView? {
        val query = Query()
        query.addCriteria(where("tableViewId").isEqualTo(aggregateId))
        return clientServicesViewMongoTemplate.findOne(query, TableView::class.java)
    }

    fun save(tableView: TableView) {
        clientServicesViewMongoTemplate.save(tableView)
    }

    fun findAllOpenRouletteTables(searchedPlayerNick: String): List<TableView> {
        val query = BasicQuery("""{ ${dollar}where: "this.playersIds.length < this.maxPlayersCount && this.playersIds.length > 0" }""")
                .addCriteria(where("gameType").isEqualTo("Roulette"))
                .addCriteria(where("firstPlayerNick").regex(".*$searchedPlayerNick.*"))
        return clientServicesViewMongoTemplate.find(query, TableView::class.java)
    }

}